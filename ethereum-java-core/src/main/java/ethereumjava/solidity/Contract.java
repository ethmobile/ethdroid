package ethereumjava.solidity;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import ethereumjava.Utils;
import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;
import ethereumjava.solidity.element.SolidityElement;
import ethereumjava.solidity.element.SolidityEvent;
import ethereumjava.solidity.element.function.SolidityFunction;

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
            Class returnType = method.getReturnType();

            if (Utils.isClassOrSubclass(returnType,SolidityFunction.class)) {
                Class<? extends SolidityFunction> classFunction = returnType;
                Object instance = classFunction.getDeclaredConstructors()[0].newInstance(address, method, eth, args);
                return classFunction.cast(instance);
            } else if (Utils.isClassOrSubclass(returnType,SolidityEvent.class)) {
                return new SolidityEvent(address, method, eth);
            }
            throw new EthereumJavaException("Contract element return type is invalid");
        }
    }

}
