package ethereumjava.solidity.types;


import java.util.regex.Pattern;

import ethereumjava.exception.EthereumJavaException;


/**
 * Created by gunicolas on 16/08/16.
 */
public class SBytes extends SType<Byte[]> {

    private SBytes(Byte[] value) {
        super(value);
    }


    public static SBytes fromByteArray(Byte[] value) {
        if (value.length > 32)
            throw new EthereumJavaException("illegal argument. SBytes is limited to 32 bytes length.");
        return new SBytes(value);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^bytes([0-9])+(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    public static int staticPartLength(String name) {
        if (isType(name)) {
            int start = "bytes".length();
            int arrayIndex = name.indexOf("[");
            if (arrayIndex == -1) arrayIndex = name.length();

            String sizeStr = name.substring(start, arrayIndex);

            return Integer.parseInt(sizeStr) * staticArrayLength(name);

        }
        return -1; //ERROR
    }

    @Override
    public boolean isDynamicType() {
        return false;
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
