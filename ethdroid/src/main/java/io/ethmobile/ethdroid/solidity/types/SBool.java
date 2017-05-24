package io.ethmobile.ethdroid.solidity.types;


import java.util.regex.Pattern;

/**
 * Created by gunicolas on 5/08/16.
 */
public class SBool extends SType<Boolean> {

    private SBool(boolean value) {
        super(value);
    }

    public static SBool fromBoolean(boolean value) {
        return new SBool(value);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^bool(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    @Override
    public String asString() {
        return value ? "1" : "0";
    }

}
