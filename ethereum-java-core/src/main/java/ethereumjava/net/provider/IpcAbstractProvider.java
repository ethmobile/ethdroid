package ethereumjava.net.provider;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.net.Request;
import ethereumjava.net.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by gunicolas on 31/08/16.
 */
public abstract class IpcAbstractProvider extends AbstractProvider {

    public final String ipcFilePath;

    Thread listeningThread;
    boolean listen;

    public IpcAbstractProvider(String _ipcFilePath) throws EthereumJavaException {
        super();
        this.ipcFilePath = _ipcFilePath;
    }

    @Override
    public void init() throws EthereumJavaException {
        startListening();
    }

    @Override
    protected Observable send(byte[] stringRequest, final Request request) {
        try {
            out.write(stringRequest);
        } catch (IOException e) {
            throw new EthereumJavaException(e);
        }
        requestQueue.put(request.getId(), request);
        Observable ret = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                request.addSubscriber(subscriber);
            }
        });
        return ret;
    }

    private void startListening() throws EthereumJavaException {
        Observable.interval(500, TimeUnit.MILLISECONDS) //Thread computation
            .map(readFile())
            .filter(removeNullLines())
            .subscribe(manageResponse());
    }

    private Action1<String> manageResponse() {
        return new Action1<String>() {
            @Override
            public void call(String line) {
                Response response = gson.fromJson(line, Response.class);

                if (response.request != null) {

                    List<Subscriber> subscribers = response.request.getSubscribers();
                    if (response.isError()) {
                        for (Subscriber subscriber : subscribers) {
                            subscriber.onError(new EthereumJavaException(response.error.message));
                        }
                    } else {
                        for (Subscriber subscriber : subscribers) {
                            subscriber.onNext(response.result);
                            subscriber.onCompleted();
                        }
                    }
                    requestQueue.remove(response.id);
                }
            }
        };
    }

    private Func1<String, Boolean> removeNullLines() {
        return new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s != null;
            }
        };
    }

    private Func1<Long, String> readFile() {
        return new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                try {
                    if (!in.ready()) {
                        return null;
                    } else {
                        String line = in.readLine();
                        return line;
                    }
                } catch (IOException e) {
                    throw new EthereumJavaException(e);
                }
            }
        };
    }

    @Override
    public void stop() throws EthereumJavaException {

        if (this.listeningThread != null) {
            this.listen = false;
        }
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            if (this.outputStream != null) {
                this.outputStream.close();
            }
        } catch (IOException e) {
            throw new EthereumJavaException(e);
        }
    }
}
