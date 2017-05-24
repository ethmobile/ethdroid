package io.ethmobile.ethdroid.solidity.coder.decoder;

/**
 * Created by gunicolas on 08/09/16.
 */
public interface SDecoder<T> {
    T decode(String toDecode);
}
