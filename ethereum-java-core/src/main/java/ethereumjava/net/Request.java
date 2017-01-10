package ethereumjava.net;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by gunicolas on 23/08/16.
 */
public class Request {

    String methodCall;
    String arguments;
    Type returnType;
    List<Subscriber> subscribers;
    int id;

    public Request(String methodCall, String arguments, Type returnType) {
        this.methodCall = methodCall;
        this.arguments = arguments;
        this.returnType = returnType;
        subscribers = new ArrayList<>();
    }

    public String getMethodCall() {
        return methodCall;
    }

    public String getArguments() {
        return arguments;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
