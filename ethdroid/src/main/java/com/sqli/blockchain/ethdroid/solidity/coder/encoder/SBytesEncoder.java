package com.sqli.blockchain.ethdroid.solidity.coder.encoder;


import com.sqli.blockchain.ethdroid.solidity.SolidityUtils;
import com.sqli.blockchain.ethdroid.solidity.types.SBytes;

/**
 * Created by gunicolas on 05/10/16.
 */

public class SBytesEncoder implements SEncoder<SBytes> {

    @Override
    public String encode(SBytes toEncode) {
        return SolidityUtils.padRightWithZeros(toEncode.asString(), 64);
    }
}
