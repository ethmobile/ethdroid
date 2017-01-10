package ethereumjava.exception;

/**
 * Created by gunicolas on 17/08/16.
 */

/**
 * EthereumJava exception that acts as a RuntimeException
 */
public class EthereumJavaException extends RuntimeException {

    public EthereumJavaException(Exception e) {
        super(e.getMessage(), e.getCause());
    }

    public EthereumJavaException(String message) {
        super(message);
    }
}
