package io.ethmobile.ethdroid.solidity.element.event;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.model.Filter;
import io.ethmobile.ethdroid.model.FilterOptions;
import io.ethmobile.ethdroid.solidity.coder.SCoder;
import io.ethmobile.ethdroid.solidity.element.SolidityElement;
import io.ethmobile.ethdroid.solidity.element.returns.SingleReturn;
import io.ethmobile.ethdroid.solidity.types.SArray;
import io.ethmobile.ethdroid.solidity.types.SType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;
import rx.Observable;


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
