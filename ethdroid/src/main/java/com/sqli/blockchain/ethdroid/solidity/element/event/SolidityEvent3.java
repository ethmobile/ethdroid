package com.sqli.blockchain.ethdroid.solidity.element.event;

import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.solidity.element.returns.PairReturn;
import com.sqli.blockchain.ethdroid.solidity.element.returns.TripleReturn;
import com.sqli.blockchain.ethdroid.solidity.types.SType;

import java.lang.reflect.Method;

import rx.functions.Func1;


/**
 * Created by gunicolas on 22/03/17.
 */

public class SolidityEvent3<T1 extends SType,T2 extends SType,T3 extends SType> extends SolidityEvent2 {

    public SolidityEvent3(String address, Method method, EthDroid eth) {
        super(address, method, eth);
    }

    @Override
    TripleReturn<T1,T2,T3> wrapDecodedLogs(SType[] decodedLogs) {
        return new TripleReturn(decodedLogs[0],decodedLogs[1],decodedLogs[2]);
    }
}
