package ethereumjava.solidity;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;

/**
 * Created by gunicolas on 30/08/16.
 */
public class Contract {

    String address;
    Eth eth;

    public Contract(Eth eth) {
        this.eth = eth;
    }

    public <T extends ContractType> ContractInstance<T> withAbi(Class<T> clazz) {
        return new ContractInstance(clazz);
    }

    public final class ContractInstance<T extends ContractType> {
        Class<T> clazz;

        public ContractInstance(Class<T> aClazz) {
            clazz = aClazz;
        }

        public T at(String address) {
            Contract.this.address = address;
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler());
        }

    }

    class InvocationHandler implements java.lang.reflect.InvocationHandler {

        @Override
        public SolidityElement invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getReturnType().isAssignableFrom(SolidityFunction.class)) {
                return new SolidityFunction(address, method, eth, args);
            } else if (method.getReturnType().isAssignableFrom(SolidityEvent.class)) {
                return new SolidityEvent(address, method, eth);
            }
            throw new EthereumJavaException("Contract element return type is invalid");
        }
    }

}
