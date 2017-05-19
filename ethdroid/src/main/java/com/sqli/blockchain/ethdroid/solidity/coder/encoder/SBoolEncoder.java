package com.sqli.blockchain.ethdroid.solidity.coder.encoder;


import com.sqli.blockchain.ethdroid.solidity.types.SBool;

/**
 * Created by gunicolas on 06/02/17.
 */

public class SBoolEncoder implements SEncoder<SBool> {
    @Override
    public String encode(SBool toEncode) {
        char value = toEncode.get() ? '1' : '0';
        return "000000000000000000000000000000000000000000000000000000000000000" + value;
    }
}
