package com.sqli.blockchain.ethdroid.solidity.element.event;

import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.solidity.element.returns.PairReturn;
import com.sqli.blockchain.ethdroid.solidity.element.returns.SingleReturn;
import com.sqli.blockchain.ethdroid.solidity.types.SType;

import java.lang.reflect.Method;

import rx.functions.Func1;


/**
 * Created by gunicolas on 22/03/17.
 */

public class SolidityEvent2<T1 extends SType,T2 extends SType> extends SolidityEvent {


    public SolidityEvent2(String address, Method method, EthDroid eth) {
        super(address, method, eth);
    }

    @Override
    PairReturn<T1,T2> wrapDecodedLogs(SType[] decodedLogs) {
        return new PairReturn(decodedLogs[0],decodedLogs[1]);
    }
}
