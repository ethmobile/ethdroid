package io.ethmobile.ethdroid.solidity.types;


import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by gunicolas on 16/08/16.
 */
public class SDynamicBytes extends SType<List<Byte>> {

    public SDynamicBytes(List<Byte> value) {
        super(value);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^bytes(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    @Override
    public String asString() {
        return value.toString();
    }


}
