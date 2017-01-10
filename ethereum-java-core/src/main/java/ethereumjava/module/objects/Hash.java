package ethereumjava.module.objects;


/**
 * Created by gunicolas on 25/08/16.
 */
public class Hash extends TransactionFormat {

    String value;

    public Hash(String value) {
        this.value = value;
    }

    public static Hash valueOf(String value) throws IllegalArgumentException {
        return new Hash(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
