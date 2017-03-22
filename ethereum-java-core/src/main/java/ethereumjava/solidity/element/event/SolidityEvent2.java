package ethereumjava.solidity.element.event;

import java.lang.reflect.Method;

import ethereumjava.module.Eth;
import ethereumjava.solidity.element.returns.PairReturn;
import ethereumjava.solidity.types.SType;
import rx.functions.Func1;

/**
 * Created by gunicolas on 22/03/17.
 */

public class SolidityEvent2<T1 extends SType,T2 extends SType> extends SolidityEvent {


    public SolidityEvent2(String address, Method method, Eth eth) {
        super(address, method, eth);
    }

    @Override
    protected Func1<SType[], PairReturn<T1,T2>> wrapDecodedLogs() {
        return new Func1<SType[],PairReturn<T1,T2>>(){
            @Override
            public PairReturn<T1,T2> call(SType[] decodedParams) {
                return new PairReturn(decodedParams[0],decodedParams[1]);
            }
        };
    }
}
