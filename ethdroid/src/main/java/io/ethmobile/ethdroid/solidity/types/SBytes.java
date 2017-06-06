package io.ethmobile.ethdroid.solidity.types;


import java.util.regex.Pattern;

import io.ethmobile.ethdroid.exception.EthDroidException;


/**
 * Created by gunicolas on 16/08/16.
 */
public class SBytes extends SType<Byte[]> {

    private SBytes(Byte[] value) {
        super(value);
    }


    public static SBytes fromByteArray(Byte[] value) {
        if (value.length > 32) {
            throw new EthDroidException("illegal argument. SBytes is limited to 32 bytes length.");
        }
        return new SBytes(value);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^bytes([0-9])+(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    @Override
    public String asString() {
        StringBuilder sb = new StringBuilder();
        for (Byte b : value) {
            if (b == null) break;
            sb.append(String.format("%02X", b & 0xff).toLowerCase());
        }
        return sb.toString();
    }
}
