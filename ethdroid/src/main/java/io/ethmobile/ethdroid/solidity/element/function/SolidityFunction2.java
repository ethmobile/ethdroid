package io.ethmobile.ethdroid.solidity.element.function;

import java.lang.reflect.Method;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.solidity.element.returns.PairReturn;
import io.ethmobile.ethdroid.solidity.types.SType;


/**
 * Created by gunicolas on 21/03/17.
 */

public class SolidityFunction2<T1 extends SType, T2 extends SType> extends SolidityFunction {


    public SolidityFunction2(String address, Method method, EthDroid eth, Object[] args) {
        super(address, method, eth, args);
    }

    @Override
    public PairReturn<T1, T2> call() throws Exception {
        SType[] decodedParams = makeCallAndDecode();
        return new PairReturn<>((T1) decodedParams[0], (T2) decodedParams[1]);
    }
}
