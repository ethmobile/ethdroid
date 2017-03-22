package ethereumjava.solidity.element;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;
import ethereumjava.module.objects.DefaultFilter;
import ethereumjava.module.objects.FilterOptions;
import ethereumjava.module.objects.Log;
import ethereumjava.solidity.coder.SCoder;
import ethereumjava.solidity.element.SolidityElement;
import ethereumjava.solidity.types.SArray;
import ethereumjava.solidity.types.SType;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by gunicolas on 4/08/16.
 */
public class SolidityEvent<T extends SType> extends SolidityElement {

    private DefaultFilter defaultFilter;

    public SolidityEvent(String address, Method method, Eth eth) {
        super(address, method, eth);
    }

    @Override
    protected List<AbstractMap.SimpleEntry<Type,SArray.Size>> getParametersType() {
        return returns;
    }

    private FilterOptions encode() {
        List<String> topics = new ArrayList<>();
        topics.add("0x" + signature());
        return new FilterOptions(topics, this.address);
    }

    public Observable<T> watch() {
        FilterOptions options = encode();
        this.defaultFilter = new DefaultFilter(options, eth);
        return this.defaultFilter.watch()
                                 .map(decodeLog());
    }

    private Func1<Log, T> decodeLog() {
        return new Func1<Log, T>() {
            @Override
            public T call(Log log) {
                if( returns.size() == 0 ) return null;

                String encodedResponse = log.data.substring(2); // Remove 0x prefix

                T[] decodedParams = (T[]) SCoder.decodeParams(encodedResponse,returns);
                if( decodedParams != null ) return decodedParams[0]; //TODO return multiple results
                else throw new EthereumJavaException("can't decode logs : "+log.toString());
            }
        };
    }

    public Observable stopWatching() {
        return this.defaultFilter.stopWatching();
    }


}
