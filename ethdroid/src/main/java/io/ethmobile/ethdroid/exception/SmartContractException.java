package io.ethmobile.ethdroid.exception;

/**
 * Created by gunicolas on 25/04/17.
 */

public class SmartContractException extends EthDroidException {


    public SmartContractException() {
        super("Exception thrown by contract");
    }

    public SmartContractException(String message) {
        super(message);
    }
}
