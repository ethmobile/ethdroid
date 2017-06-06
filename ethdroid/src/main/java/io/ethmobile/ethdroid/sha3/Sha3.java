package io.ethmobile.ethdroid.sha3;

import java.util.Formatter;

/**
 * Created by gunicolas on 11/08/16.
 * From : https://github.com/romus/sha/blob/master/sha3/src/com/theromus/example/Main.java
 */
public abstract class Sha3 {

    public static String hash(String value) {
        byte[] b = value.getBytes();
        String s = getHexStringByByteArray(b);

        Keccak keccak = new Keccak(1600);
        return keccak.getHash(s, 1088, 32);

    }

    private static String getHexStringByByteArray(byte[] array) {
        if (array == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 2);
        @SuppressWarnings("resource")
        Formatter formatter = new Formatter(stringBuilder);
        for (byte tempByte : array) {
            formatter.format("%02x", tempByte);
        }

        return stringBuilder.toString();
    }

}
