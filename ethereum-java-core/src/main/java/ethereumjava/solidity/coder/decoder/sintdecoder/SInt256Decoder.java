package ethereumjava.solidity.coder.decoder.sintdecoder;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.coder.decoder.SDecoder;
import ethereumjava.solidity.types.SInt;

/**
 * Created by gunicolas on 17/03/17.
 */

public class SInt256Decoder implements SDecoder<SInt.SInt256> {

    @Override
    public SInt.SInt256 decode(String toDecode) {
        if( !toDecode.toLowerCase().startsWith("0x") ){
            toDecode = "0x"+toDecode;
        }
        return SInt.fromBigInteger256(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger());
    }
}
