package ethereumjava.module.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ethereumjava.module.Eth;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by gunicolas on 20/10/16.
 */
public abstract class AbstractFilter<T> {

    static final long POLLING_TIMEOUT = 1000 / 2;

    Eth eth;

    String filterId;

    Thread pollingThread;

    List<Subscriber> filterCallbacks;

    public AbstractFilter(Eth eth) {
        this.eth = eth;
        this.filterCallbacks = new ArrayList<>();
    }

    public Observable<T> watch() {
        return Observable.interval(POLLING_TIMEOUT, TimeUnit.MILLISECONDS)
            .flatMap(getFilterChanges())
            .filter(nonEmptyList())
            .flatMap(fromListToElement());
    }

    private Func1<List<T>, Boolean> nonEmptyList() {
        return new Func1<List<T>, Boolean>() {
            @Override
            public Boolean call(List<T> ts) {
                return ts.size() > 0;
            }
        };
    }

    private Func1<List<T>, Observable<T>> fromListToElement() {
        return new Func1<List<T>, Observable<T>>() {
            @Override
            public Observable<T> call(List<T> ts) {
                return Observable.from(ts);
            }
        };
    }

    private Func1<Long, Observable<List<T>>> getFilterChanges() {
        return new Func1<Long, Observable<List<T>>>() {
            @Override
            public Observable<List<T>> call(Long l) {
                return eth.getFilterChanges(filterId, getLogType());
            }
        };
    }

    protected abstract Class<T> getLogType();

    public Observable stopWatching() {
        this.pollingThread.interrupt();
        this.filterCallbacks.clear();
        return eth.uninstallFilter(filterId);
    }
}
