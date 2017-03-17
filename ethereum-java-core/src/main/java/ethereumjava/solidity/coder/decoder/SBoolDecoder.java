package ethereumjava.solidity.coder.decoder;

import ethereumjava.solidity.types.SBool;
import ethereumjava.solidity.types.SType;

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
