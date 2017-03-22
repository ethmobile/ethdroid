package ethereumjava.solidity.element.function;

import java.lang.reflect.Method;

import ethereumjava.module.Eth;
import ethereumjava.solidity.element.returns.PairReturn;
import ethereumjava.solidity.types.SType;

/**
 * Created by gunicolas on 21/03/17.
 */

public class SolidityFunction2<T1 extends SType, T2 extends SType> extends SolidityFunction {


    public SolidityFunction2(String address, Method method, Eth eth, Object[] args) {
        super(address, method, eth, args);
    }

    @Override
    public PairReturn<T1,T2> call() {
        SType[] decodedParams = makeCallAndDecode();
        return new PairReturn(decodedParams[0],decodedParams[1]);
    }
}
