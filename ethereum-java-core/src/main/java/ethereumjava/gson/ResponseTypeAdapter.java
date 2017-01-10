package ethereumjava.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

import ethereumjava.net.Request;
import ethereumjava.net.Response;

/**
 * Created by gunicolas on 25/08/16.
 */
public class ResponseTypeAdapter<T> implements JsonSerializer<Response<T>>, JsonDeserializer<Response<T>> {

    Map<Integer, Request> requestQueue;

    public ResponseTypeAdapter(Map<Integer, Request> _requestQueue) {
        requestQueue = _requestQueue;
    }

    @Override
    public Response<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject requestJson = json.getAsJsonObject();
        int requestId = requestJson.get("id").getAsInt();
        Request request = requestQueue.get(requestId);
        Type returnType = request.getReturnType();

        T result = context.deserialize(requestJson.get("result"), returnType);
        Response<T>.Error error = context.deserialize(requestJson.get("error"), Response.Error.class);

        return new Response<>(requestId, result, error, request);
    }

    @Override
    public JsonElement serialize(Response src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }

}
