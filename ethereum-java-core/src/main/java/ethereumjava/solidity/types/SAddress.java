package ethereumjava.solidity.types;

import java.util.regex.Pattern;

import ethereumjava.exception.EthereumJavaException;

/**
 * Created by gunicolas on 5/08/16.
 */
public class SAddress extends SType<String> {

    private SAddress(String address) {
        super(address);
        //InputIntFormatter(),
        //OuputAddressFormatter()
    }

    public static SAddress fromString(String from) throws EthereumJavaException {
        if (!isAddress(from))
            throw new EthereumJavaException("illegal argument. " + from + " is not a solidity address");
        return new SAddress(from);
    }

    public static boolean isAddress(String value) {
        return Pattern.compile("^(0x)?([0-9a-fA-F]){40}$").matcher(value).matches();
    }

    public static boolean isType(String name) {
        return Pattern.compile("^address(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    public static int staticPartLength(String name) {
        return 32 * staticArrayLength(name);
    }

    @Override
    public boolean isDynamicType() {
        return false;
    }

    @Override
    public String asString() {
        return value;
    }

}
