package ethereumjava.solidity.coder.decoder.suintdecoder;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.coder.decoder.SDecoder;
import ethereumjava.solidity.types.SInt;
import ethereumjava.solidity.types.SUInt;

/**
 * Created by gunicolas on 17/03/17.
 */

public class SUInt128Decoder implements SDecoder<SUInt.SUInt128> {

    @Override
    public SUInt.SUInt128 decode(String toDecode) {
        if( !toDecode.toLowerCase().startsWith("0x") ){
            toDecode = "0x"+toDecode;
        }
        return SUInt.fromBigInteger128(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger());
    }
}
