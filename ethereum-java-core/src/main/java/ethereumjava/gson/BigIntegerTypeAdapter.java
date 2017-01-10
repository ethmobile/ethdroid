package ethereumjava.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by gunicolas on 24/08/16.
 */
public class BigIntegerTypeAdapter extends TypeAdapter<BigInteger> {

    @Override
    public void write(JsonWriter out, BigInteger value) throws IOException {

    }

    @Override
    public BigInteger read(JsonReader in) throws IOException {
        String hexa = in.nextString();
        hexa = hexa.replace("0x", "");
        return new BigInteger(hexa, 16);
    }
}
