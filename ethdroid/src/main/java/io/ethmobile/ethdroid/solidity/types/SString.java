package io.ethmobile.ethdroid.solidity.types;


import java.util.regex.Pattern;

/**
 * Created by gunicolas on 16/08/16.
 */
public class SString extends SType<String> {

    private SString(String value) {
        super(value);
    }

    public static SString fromString(String from) {
        return new SString(from);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^string(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    @Override
    public String asString() {
        return value;
    }

}
