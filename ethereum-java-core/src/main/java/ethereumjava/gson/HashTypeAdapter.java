package ethereumjava.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ethereumjava.module.objects.Hash;

/**
 * Created by gunicolas on 25/08/16.
 */
public class HashTypeAdapter extends TypeAdapter<Hash> {
    @Override
    public void write(JsonWriter out, Hash value) throws IOException {

    }

    @Override
    public Hash read(JsonReader in) throws IOException {
        return new Hash(in.nextString());
    }
}
