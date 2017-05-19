package com.sqli.blockchain.ethdroid.solidity.coder.decoder;


import com.sqli.blockchain.ethdroid.solidity.types.SBool;
import com.sqli.blockchain.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 06/02/17.
 */

public class SBoolDecoder implements SDecoder<SBool> {

    @Override
    public SBool decode(String toDecode) {
        boolean value = toDecode.toCharArray()[SType.ENCODED_SIZE-1] == '1';
        return SBool.fromBoolean(value);
    }
}
