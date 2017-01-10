package ethereumjava.solidity.types;


import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by gunicolas on 16/08/16.
 */
public class SDynamicBytes extends SType<List<Byte>> {

    public SDynamicBytes(List<Byte> value) {
        super(value);
        //InputDynamicBytesFormatter()
        //OutputDynamicBytesFormatter()
    }

    public static boolean isType(String name) {
        return Pattern.compile("^bytes(\\[([0-9])*\\])*$").matcher(name).matches();
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
        return value.toString();
    }


}
