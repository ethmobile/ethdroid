package com.sqli.blockchain.ethdroid.solidity;

import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.Utils;
import com.sqli.blockchain.ethdroid.solidity.element.SolidityElement;
import com.sqli.blockchain.ethdroid.solidity.element.event.SolidityEvent;
import com.sqli.blockchain.ethdroid.solidity.element.function.SolidityFunction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by gunicolas on 30/08/16.
 */
public class Contract<T extends ContractType> implements InvocationHandler {

    private String address;
    private EthDroid eth;
    private T instance;

    private Contract(EthDroid eth,Class<T> contractAbi,String address){
        this.address = address;
        this.eth = eth;
        this.instance = (T) Proxy.newProxyInstance( contractAbi.getClassLoader(),
                                                        new Class[]{contractAbi},
                                                        this);
    }

    private T getInstance(){
        return instance;
    }

    public static <T extends ContractType> T getContractInstance(EthDroid eth,Class<T> contractAbi,String address) {
        return new Contract<>(eth, contractAbi, address).getInstance();
    }

    @Override
    public SolidityElement invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class returnType = method.getReturnType();

        if (Utils.isClassOrSubclass(returnType,SolidityFunction.class)) {
            Class<? extends SolidityFunction> classFunction = returnType;
            Object instance = classFunction.getDeclaredConstructors()[0].newInstance(address, method, eth, args);
            return classFunction.cast(instance);
        } else if (Utils.isClassOrSubclass(returnType,SolidityEvent.class)) {
            Class<? extends SolidityEvent> classEvent = returnType;
            Object instance = classEvent.getDeclaredConstructors()[0].newInstance(address, method, eth);
            return classEvent.cast(instance);
        }
        throw new Exception("Contract element return type is invalid");
    }

}
