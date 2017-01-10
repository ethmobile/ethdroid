package ethereumjava.net.provider;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.net.Request;
import rx.Observable;

/**
 * Created by gunicolas on 23/08/16.
 */
public interface Provider {

    /**
     * send the given Request through a specific communication protocol and return an observable on the parameterized response.
     * @param request request object to send {@link Request}
     * @param <T> response type
     * @return By contract, cannot be <code>null</code>. The @Observable to subscribe to get the
     * response.
     */
    <T> Observable<T> sendRequest(Request request);

    void init() throws EthereumJavaException;

    void stop() throws EthereumJavaException;

}
