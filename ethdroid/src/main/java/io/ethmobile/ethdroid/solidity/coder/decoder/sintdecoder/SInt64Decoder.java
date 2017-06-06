package io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder;

import io.ethmobile.ethdroid.solidity.SolidityUtils;
import io.ethmobile.ethdroid.solidity.coder.decoder.SDecoder;
import io.ethmobile.ethdroid.solidity.types.SInt;

/**
 * Created by gunicolas on 17/03/17.
 */

public class SInt64Decoder implements SDecoder<SInt.SInt64> {

    @Override
    public SInt.SInt64 decode(String toDecode) {
        if (!toDecode.toLowerCase().startsWith("0x")) {
            toDecode = "0x" + toDecode;
        }
        return SInt.fromLong(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger().longValue());
    }
}
