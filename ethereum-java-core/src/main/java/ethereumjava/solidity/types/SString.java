package ethereumjava.solidity.types;


import java.util.regex.Pattern;

/**
 * Created by gunicolas on 16/08/16.
 */
public class SString extends SType<String> {

    private SString(String value) {
        super(value);
        //InputStringFormatter()
        //OuputStringFormatter()
    }

    public static SString fromString(String from) {
        return new SString(from);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^string(\\[([0-9])*\\])*$").matcher(name).matches();
    }

    public static int staticPartLength(String name) {
        return 32 * staticArrayLength(name);
    }

    @Override
    public boolean isDynamicType() {
        return true;
    }

    @Override
    public String asString() {
        return value;
    }


}
