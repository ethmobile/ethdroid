package io.ethmobile.ethdroid.model;

import org.ethereum.geth.FilterLogsHandler;
import org.ethereum.geth.Header;
import org.ethereum.geth.Log;
import org.ethereum.geth.NewHeadHandler;
import org.ethereum.geth.Subscription;

import io.ethmobile.ethdroid.EthDroid;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gunicolas on 22/05/17.
 */

public class Filter {

    private static final Scheduler DEFAULT_SCHEDULER = Schedulers.newThread();
    private static final long DEFAULT_BUFFER_SIZE = 16; //See go-ethereum tests https://git.io/v9X1O

    public static Observable<Header> newHeadFilter(EthDroid eth) {
        return Observable.create(new HeadFilterSubscriber(eth))
            .subscribeOn(DEFAULT_SCHEDULER);
    }

    public static Observable<Log> newLogFilter(EthDroid eth, FilterOptions options) {
        return Observable.create(new LogFilterSubscriber(eth, options))
            .subscribeOn(DEFAULT_SCHEDULER);
    }

    private static class HeadFilterSubscriber implements ObservableOnSubscribe<Header> {

        EthDroid eth;

        HeadFilterSubscriber(EthDroid eth) {
            this.eth = eth;
        }

        @Override
        public void subscribe(ObservableEmitter<Header> emitter) throws Exception {
            HeadListener listener = new HeadListener(emitter);
            try {
                listener.subscription = eth.getClient().subscribeNewHead(eth.getMainContext(),
                    listener, DEFAULT_BUFFER_SIZE);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }

    private static class HeadListener implements NewHeadHandler {

        ObservableEmitter<? super Header> emitter;
        Subscription subscription;

        public HeadListener(ObservableEmitter<? super Header> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onError(String s) {
            if (this.emitter.isDisposed()) {
                subscription.unsubscribe();
            } else {
                this.emitter.onError(new Throwable(s));
            }
        }

        @Override
        public void onNewHead(Header header) {
            if (this.emitter.isDisposed()) {
                subscription.unsubscribe();
            } else {
                this.emitter.onNext(header);
            }
        }
    }

    private static class LogFilterSubscriber implements ObservableOnSubscribe<Log> {

        EthDroid eth;
        FilterOptions filterOptions;

        public LogFilterSubscriber(EthDroid eth, FilterOptions filterOptions) {
            this.eth = eth;
            this.filterOptions = filterOptions;
        }

        @Override
        public void subscribe(ObservableEmitter<Log> emitter) throws Exception {
            LogListener listener = new LogListener(emitter);
            try {
                listener.subscription = eth.getClient().subscribeFilterLogs(eth.getMainContext(),
                    filterOptions.getQuery(), listener, DEFAULT_BUFFER_SIZE);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }

    private static class LogListener implements FilterLogsHandler {

        ObservableEmitter<? super Log> emitter;
        Subscription subscription;

        public LogListener(ObservableEmitter<? super Log> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onError(String s) {
            if (this.emitter.isDisposed()) {
                subscription.unsubscribe();
            } else {
                this.emitter.onError(new Throwable(s));
            }
        }

        @Override
        public void onFilterLogs(Log log) {
            if (this.emitter.isDisposed()) {
                subscription.unsubscribe();
            } else {
                this.emitter.onNext(log);
            }
        }
    }

}
