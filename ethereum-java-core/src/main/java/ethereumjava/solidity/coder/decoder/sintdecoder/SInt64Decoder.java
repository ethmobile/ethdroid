package ethereumjava.solidity.coder.decoder.sintdecoder;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.coder.decoder.SDecoder;
import ethereumjava.solidity.types.SInt;

/**
 * Created by gunicolas on 17/03/17.
 */

public class SInt64Decoder implements SDecoder<SInt.SInt64> {

    @Override
    public SInt.SInt64 decode(String toDecode) {
        if( !toDecode.toLowerCase().startsWith("0x") ){
            toDecode = "0x"+toDecode;
        }
        return SInt.fromLong(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger().longValue());
    }
}
