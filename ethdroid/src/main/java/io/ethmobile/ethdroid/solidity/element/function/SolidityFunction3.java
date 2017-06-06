package io.ethmobile.ethdroid.solidity.element.function;

import java.lang.reflect.Method;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.solidity.element.returns.TripleReturn;
import io.ethmobile.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 22/03/17.
 */

public class SolidityFunction3<T1 extends SType, T2 extends SType, T3 extends SType> extends
    SolidityFunction2 {

    public SolidityFunction3(String address, Method method, EthDroid eth, Object[] args) {
        super(address, method, eth, args);
    }

    @Override
    public TripleReturn<T1, T2, T3> call() throws Exception {
        SType[] decodedParams = makeCallAndDecode();
        return new TripleReturn<>((T1) decodedParams[0], (T2) decodedParams[1],
            (T3) decodedParams[2]);
    }
}
