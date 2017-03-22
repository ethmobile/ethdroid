package ethereumjava.solidity.element.event;

import java.lang.reflect.Method;

import ethereumjava.module.Eth;
import ethereumjava.solidity.element.returns.TripleReturn;
import ethereumjava.solidity.types.SType;
import rx.functions.Func1;

/**
 * Created by gunicolas on 22/03/17.
 */

public class SolidityEvent3<T1 extends SType,T2 extends SType,T3 extends SType> extends SolidityEvent2 {

    public SolidityEvent3(String address, Method method, Eth eth) {
        super(address, method, eth);
    }

    @Override
    protected Func1<SType[], TripleReturn<T1,T2,T3>> wrapDecodedLogs() {
        return new Func1<SType[], TripleReturn<T1, T2, T3>>() {
            @Override
            public TripleReturn<T1, T2, T3> call(SType[] decodedParams) {
                return new TripleReturn(decodedParams[0],decodedParams[1],decodedParams[2]);
            }
        };
    }
}
