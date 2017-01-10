package ethereumjava.solidity.types;

/**
 * Created by gunicolas on 12/10/16.
 */
public class SVoid extends SType<Void> {

    public SVoid() {
        super(null);
    }

    @Override
    public boolean isDynamicType() {
        return false;
    }

    @Override
    public String asString() {
        return "";
    }
}
