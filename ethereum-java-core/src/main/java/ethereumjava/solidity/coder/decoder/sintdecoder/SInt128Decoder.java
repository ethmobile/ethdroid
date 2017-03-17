package ethereumjava.solidity.coder.decoder.sintdecoder;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.coder.decoder.SDecoder;
import ethereumjava.solidity.types.SInt;

/**
 * Created by gunicolas on 17/03/17.
 */

public class SInt128Decoder implements SDecoder<SInt.SInt128> {

    @Override
    public SInt.SInt128 decode(String toDecode) {
        if( !toDecode.toLowerCase().startsWith("0x") ){
            toDecode = "0x"+toDecode;
        }
        return SInt.fromBigInteger128(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger());
    }
}
