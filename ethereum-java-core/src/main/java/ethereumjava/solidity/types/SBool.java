package ethereumjava.solidity.types;


import java.util.regex.Pattern;

/**
 * Created by gunicolas on 5/08/16.
 */
public class SBool extends SType<Boolean> {

    private SBool(boolean value) {
        super(value);
        //InputBoolFormatter(),
        //OutputBoolFormatter());
    }


    public static SBool fromBoolean(boolean value) {
        return new SBool(value);
    }

    public static boolean isType(String name) {
        return Pattern.compile("^bool(\\[([0-9])*\\])*$").matcher(name).matches();
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
        return value ? "1" : "0";
    }


}
