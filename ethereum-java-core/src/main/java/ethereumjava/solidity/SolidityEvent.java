package ethereumjava.solidity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;
import ethereumjava.module.objects.DefaultFilter;
import ethereumjava.module.objects.FilterOptions;
import ethereumjava.module.objects.Log;
import ethereumjava.solidity.coder.SCoderMapper;
import ethereumjava.solidity.coder.decoder.SDecoder;
import ethereumjava.solidity.types.SType;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by gunicolas on 4/08/16.
 */
public class SolidityEvent<T> extends SolidityElement {

    Class<? extends SDecoder<T>> decoder;
    DefaultFilter defaultFilter;

    public SolidityEvent(String address, Method method, Eth eth) {
        super(address, method, eth);

        Class[] returnParams = getParametersTypes();
        if (returnParams.length > 0) {
            decoder = (Class<? extends SDecoder<T>>) SCoderMapper.getDecoderForClass(returnParams[0]); //TODO remove cast
        }
    }

    @Override
    protected Class[] getParametersTypes() {

        List<Class> ret = new ArrayList<>();

        Parameters parameters = method.getAnnotation(Parameters.class);

        if (parameters != null) {
            for (Parameter parameter : parameters.value()) {
                ret.add(parameter.type());
            }
        }

        return ret.toArray(new Class[ret.size()]);
    }

    public FilterOptions encode() {
        List<String> topics = new ArrayList<>();
        topics.add("0x" + this.signature());
        return new FilterOptions(topics, this.address);
    }

    public Observable<T> watch() {
        FilterOptions options = encode();
        return new DefaultFilter(options, eth)
            .watch()
            .map(decodeLog());
    }

    private Func1<Log, T> decodeLog() {
        return new Func1<Log, T>() {
            @Override
            public T call(Log log) {
                try {
                    return decoder.newInstance().decode(log.data);
                } catch (Exception e) { //TODO remove InstantiationException, IllegalAccessException. Decoder must be static
                    throw new EthereumJavaException(e);
                }
            }
        };
    }

    public Observable stopWatching() {
        return this.defaultFilter.stopWatching();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Anonymous {
        boolean value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Parameters {
        Parameter[] value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Parameter {
        boolean indexed();

        String name();

        Class<? extends SType> type();
    }

}
