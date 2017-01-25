package ethereumjava.module.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

/**
 * Created by gunicolas on 14/10/16.
 */
public class FilterOptions {

    List<String> topics;
    String address;

    public FilterOptions(List<String> topics, String address) {
        this.topics = topics;
        this.address = address;
    }


    @Override
    public String toString() {

        //TODO move code to "serialise" function

        JsonObject jsonObject = new JsonObject();

        JsonArray topicsArrayJson = new JsonArray();
        for(String topic : topics){
            topicsArrayJson.add(topic);
        }

        if (topics != null) jsonObject.add("topics", topicsArrayJson);
        if (address != null) jsonObject.add("address", new JsonPrimitive(address));

        return jsonObject.toString();
    }
}
