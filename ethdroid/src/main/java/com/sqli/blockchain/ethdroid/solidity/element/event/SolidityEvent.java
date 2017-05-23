package com.sqli.blockchain.ethdroid.solidity.element.event;

import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.model.Filter;
import com.sqli.blockchain.ethdroid.model.FilterOptions;
import com.sqli.blockchain.ethdroid.solidity.coder.SCoder;
import com.sqli.blockchain.ethdroid.solidity.element.SolidityElement;
import com.sqli.blockchain.ethdroid.solidity.element.returns.SingleReturn;
import com.sqli.blockchain.ethdroid.solidity.types.SArray;
import com.sqli.blockchain.ethdroid.solidity.types.SType;

import org.ethereum.geth.Address;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by gunicolas on 4/08/16.
 */
public class SolidityEvent<T extends SType> extends SolidityElement {

    public SolidityEvent(String address, Method method, EthDroid eth) {
        super(address, method, eth);
    }

    @Override
    protected List<AbstractMap.SimpleEntry<Type,SArray.Size>> getParametersType() {
        return returns;
    }

    private FilterOptions encode() throws Exception {
        List<String> topics = new ArrayList<>();
        topics.add("0x" + signature());
        return new FilterOptions()
            .addTopics(topics)
            .addAddress(this.address);
    }

    Observable<SType[]> createFilterAndDecode() throws Exception {
        FilterOptions options = encode();

        return Filter.newLogFilter(eth,options)
                    .map(log -> {
                        if( returns.size() == 0 ) return null;
                        else return SCoder.decodeParams(ByteString.of(log.getData()).hex(),returns);
                    });
    }


    public Observable<SingleReturn<T>> listen() throws Exception {
        return createFilterAndDecode().map(this::wrapDecodedLogs);
    }

    SingleReturn<T> wrapDecodedLogs(SType[] decodedLogs) {
        return new SingleReturn(decodedLogs[0]);
    }

}
