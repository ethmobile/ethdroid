package io.ethmobile.ethdroid.solidity.coder.encoder;

/**
 * Created by gunicolas on 08/09/16.
 */
public interface SEncoder<T> {

    String encode(T toEncode);

}
