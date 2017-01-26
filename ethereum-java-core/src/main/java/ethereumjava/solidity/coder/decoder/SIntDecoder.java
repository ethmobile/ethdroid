package ethereumjava.solidity.coder.decoder;

import java.math.BigInteger;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.types.SInt;

/**
 * Created by gunicolas on 08/09/16.
 */
public abstract class SIntDecoder implements SDecoder<SInt> {

    @Override
    public SInt decode(String toDecode) {
        return SInt.fromBigInteger256(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger());
    }
}
