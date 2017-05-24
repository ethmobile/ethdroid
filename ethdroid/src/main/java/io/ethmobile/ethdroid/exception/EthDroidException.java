package io.ethmobile.ethdroid.exception;

/**
 * Created by gunicolas on 17/08/16.
 */

/**
 * EthereumJava exception that acts as a RuntimeException
 */
public class EthDroidException extends RuntimeException {

    public EthDroidException(Exception e) {
        super(e.getMessage(), e.getCause());
    }

    public EthDroidException(String message) {
        super(message);
    }
}
