package ethereumjava.net.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.gson.BigIntegerTypeAdapter;
import ethereumjava.gson.HashTypeAdapter;
import ethereumjava.gson.ResponseTypeAdapter;
import ethereumjava.module.objects.Hash;
import ethereumjava.net.Request;
import ethereumjava.net.Response;
import rx.Observable;

/**
 * Created by gunicolas on 17/08/16.
 */
public abstract class AbstractProvider implements Provider {

    protected static final Charset CHARSET = StandardCharsets.UTF_8;

    protected Map<Integer, Request> requestQueue;
    protected OutputStream outputStream;
    protected InputStream inputStream;
    protected DataOutputStream out;
    protected BufferedReader in;
    Gson gson;
    private int requestNumber = 0;

    public AbstractProvider() throws EthereumJavaException {
        requestQueue = new HashMap<>();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(BigInteger.class, new BigIntegerTypeAdapter());
        gsonBuilder.registerTypeHierarchyAdapter(Response.class, new ResponseTypeAdapter(requestQueue));
        gsonBuilder.registerTypeHierarchyAdapter(Hash.class, new HashTypeAdapter());
        gson = gsonBuilder.create();
    }


    @Override
    public Observable sendRequest(final Request request) throws EthereumJavaException {

        request.setId(getARequestNumber());

        String stringRequest = "{" +
            "\"jsonrpc\":\"2.0\"," +
            "\"method\":\"" + request.getMethodCall() + "\"," +
            "\"params\":" + request.getArguments() + "," +
            "\"id\":" + request.getId() + "" +
            "}";

        byte[] req = stringRequest.getBytes(CHARSET);

        return send(req, request);
    }

    protected abstract Observable send(byte[] stringRequest, Request request);

    protected synchronized int getARequestNumber() {
        return requestNumber++;
    }

}
