package com.sqli.blockchain.ethdroid.solidity.element.event;

import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.solidity.coder.SCoder;
import com.sqli.blockchain.ethdroid.solidity.element.SolidityElement;
import com.sqli.blockchain.ethdroid.solidity.element.returns.SingleReturn;
import com.sqli.blockchain.ethdroid.solidity.types.SArray;
import com.sqli.blockchain.ethdroid.solidity.types.SType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by gunicolas on 4/08/16.
 */
public class SolidityEvent<T extends SType> extends SolidityElement {

    //private DefaultFilter defaultFilter;

    public SolidityEvent(String address, Method method, EthDroid eth) {
        super(address, method, eth);
    }

    @Override
    protected List<AbstractMap.SimpleEntry<Type,SArray.Size>> getParametersType() {
        return returns;
    }

    /*private FilterOptions encode() {
        List<String> topics = new ArrayList<>();
        topics.add("0x" + signature());
        return new FilterOptions(topics, this.address);
    }

    Observable<SType[]> createFilterAndDecode(){
        FilterOptions options = encode();
        this.defaultFilter = new DefaultFilter(options, eth);
        return this.defaultFilter.watch()
                                .map(cleanLogs())
                                .map(decodeLog());
    }

    public Observable<SingleReturn<T>> watch() {
        return createFilterAndDecode().map(wrapDecodedLogs());
    }

    private Func1<Log,String> cleanLogs(){
        return new Func1<Log,String>() {
            @Override
            public String call(Log log) {
                return log.data.substring(2); // Remove 0x prefix
            }
        };
    }

    private Func1<String, SType[]> decodeLog() {
        return new Func1<String, SType[]>() {
            @Override
            public SType[] call(String log) {
                if( returns.size() == 0 ) return null;
                else return SCoder.decodeParams(log,returns);

            }
        };
    }

    protected Func1<SType[],SingleReturn<T>> wrapDecodedLogs(){
        return new Func1<SType[],SingleReturn<T>>(){
            @Override
            public SingleReturn<T> call(SType[] decodedParams) {
                return new SingleReturn(decodedParams[0]);
            }
        };
    }

    public Observable stopWatching() {
        return this.defaultFilter.stopWatching();
    }
*/

}
