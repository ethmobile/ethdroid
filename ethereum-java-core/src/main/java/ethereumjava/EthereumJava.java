package ethereumjava;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Admin;
import ethereumjava.module.Eth;
import ethereumjava.module.Personal;
import ethereumjava.module.annotation.ConvertParam;
import ethereumjava.module.annotation.ExcludeFromRequest;
import ethereumjava.module.converter.ParameterConverter;
import ethereumjava.net.Request;
import ethereumjava.net.provider.Provider;
import ethereumjava.solidity.Contract;
import rx.Observable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Entry class. Allows managment of a Geth node via Remote Pocedure Call or Inter-process communication.
 *      It can :
 *      <ul>
 *          <li>Communicates with Geth exposed interfaces : {@link Admin Admin}, {@link Eth Eth}, {@link Personal personal}.</li>
 *          <li>Call a smart-contract deployed on the Ethereum blockchain with the {@link Contract Contract} module.</li>
 *      </ul>
 *      It can be personalized with its builder, like define the provider and its parameters.
 *      Instanciate EthereumJava thanks to the {@link Builder Builder} like this :
 *      <pre>
 *      {@code
 *          EthereumJava ethereumJava = new EthereumJava.Builder()
 *              .provider(new RpcProvider("http://localhost:8545"))
 *              .build();
 *      }
 *      </pre>
 *
 *      Then, modules are accessibles directly :
 *       <pre>
 *       {@code
 *          ethereumJava.eth.[...]
 *          ethereumJava.admin.[...]
 *          ethereumJava.personal.[...]
 *        }
 *        </pre>
 */
public class EthereumJava {

    /**
     * The Admin module, giving accesses to admin methods.
     *
     * @see Admin
     */
    public Admin admin;
    /**
     * The Personal module, giving accesses to personal methods.
     *
     * @see Personal
     */
    public Personal personal;
    /**
     * The Eth module, giving accesses to eth methods.
     *
     * @see Personal
     */
    public Eth eth;
    /**
     * The Contract module, allowing to communicate with a deployed contract on Ethereum blockchain.
     *
     * @see Contract
     */
    public Contract contract;

    private Provider provider;

    private EthereumJava(Provider provider, Admin admin, Personal personal, Eth eth, Contract contract) {
        this.admin = admin;
        this.personal = personal;
        this.eth = eth;
        this.contract = contract;
        this.provider = provider;
    }

    public void close() {
        provider.stop();
    }

    /**
     * Builder parameterize EthereumJava instance.
     * You can set the provider, then build to get the instance.
     * <p>
     * <b>Caution:</b> there is no default provider.
     */
    public static class Builder {

        private InvocationHandler handler;
        private Provider provider;

        public Builder() {
        }

        /**
         * Set EthereumJava's provider.
         *
         * @param provider instance of a Provider
         * @return Builder class used with Builder pattern
         */
        public Builder provider(Provider provider) {
            this.provider = provider;
            handler = new InvocationHandler(this.provider);
            return this;
        }

        /**
         * Build EthereumJava with its parameters and returns the parameterized instance.
         *
         * @return EthereumJava parameterized instance
         * @throws EthereumJavaException if provider is not set. Use builder.provider([...]) to set
         *                               the provider
         */
        public EthereumJava build() throws EthereumJavaException {
            if (this.provider == null || handler == null) {
                throw new EthereumJavaException("Missing provider");
            }
            provider.init();
            Admin admin = (Admin) Proxy.newProxyInstance(Admin.class.getClassLoader(), new Class[]{Admin.class}, handler);
            Personal personal = (Personal) Proxy.newProxyInstance(Personal.class.getClassLoader(), new Class[]{Personal.class}, handler);
            Eth eth = (Eth) Proxy.newProxyInstance(Eth.class.getClassLoader(), new Class[]{Eth.class}, handler);
            Contract contract = new Contract(eth);
            return new EthereumJava(provider, admin, personal, eth, contract);
        }

    }

    static class InvocationHandler implements java.lang.reflect.InvocationHandler {

        Provider provider;

        public InvocationHandler(Provider _provider) {
            this.provider = _provider;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Type returnType = Utils.extractReturnType(method, args); //Must extract from originial parameters (next instruction convert these parameters)

            if (args != null) {
                args = convertArgumentsIfNecessary(method, args);
            }
            String formattedArgs = Utils.formatArgsToString(args);
            String moduleName = method.getDeclaringClass().getSimpleName().toLowerCase();
            String methodName = "_" + Utils.extractMethodName(method);


            Request request = new Request(moduleName + methodName, formattedArgs, returnType);

            Observable<?> result = provider.sendRequest(request);
            return convertToSyncIfNecessary(result, method);

        }

        private Object[] convertArgumentsIfNecessary(Method method, Object[] args) {
            List<Object> arguments = new ArrayList<>(Arrays.asList(args));
            int i = 0;
            for (Annotation[] parameter : method.getParameterAnnotations()) {
                List<Annotation> parameterAnnotations = Arrays.asList(parameter);
                for (Annotation annotation : parameterAnnotations) {
                    if (annotation.annotationType().isAssignableFrom(ExcludeFromRequest.class)) {
                        arguments.remove(i);
                    } else if (annotation.annotationType().isAssignableFrom(ConvertParam.class)) {
                        ConvertParam convertParamAnnotation = (ConvertParam) annotation;
                        try {
                            ParameterConverter parameterConverter = convertParamAnnotation.with().newInstance();
                            arguments.set(i, parameterConverter.convertFrom(args[i]));
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new EthereumJavaException(e);
                        }
                    }
                }
                i++;
            }
            return arguments.toArray();
        }

        private <T> Object convertToSyncIfNecessary(Observable<T> requestResult, Method method) throws EthereumJavaException {
            Object result = null;
            if (method.getReturnType().isAssignableFrom(Observable.class)) {
                result = requestResult;
            } else if (requestResult != null) {
                try {
                    result = requestResult.toBlocking().single(); //TODO subscribeOn (thread current) / observeOn (pool thread)
                } catch (NoSuchElementException e) {
                    throw new EthereumJavaException(e);
                }
            }
            return result;
        }


    }


}
