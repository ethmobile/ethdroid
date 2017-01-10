package ethereumjava.solidity.coder.decoder;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.types.SUInt;

/**
 * Created by gunicolas on 13/10/16.
 */
public class SUIntDecoder implements SDecoder<SUInt> {

    @Override
    public SUInt decode(String toDecode) {
        return SUInt.SUInt256.fromBigInteger256(SolidityUtils.hexToBigDecimal(toDecode).toBigInteger());
    }
}
