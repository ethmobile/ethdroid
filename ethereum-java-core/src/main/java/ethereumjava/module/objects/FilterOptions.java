package ethereumjava.module.objects;

import org.json.JSONObject;

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

        JSONObject jsonObject = new JSONObject();

        if (topics != null) jsonObject.put("topics", topics);
        if (address != null) jsonObject.put("address", address);

        return jsonObject.toString();
    }
}
