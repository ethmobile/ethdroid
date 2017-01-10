package ethereumjava.config;

import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

/**
 * Created by gunicolas on 23/11/16.
 */
public abstract class RxJavaHelper {


    public static void waitTerminalEvent(TestSubscriber subscriber) {
        subscriber.awaitTerminalEvent(5, TimeUnit.SECONDS);
    }
}
