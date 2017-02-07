package ethereumjava.solidity.coder.decoder;

import ethereumjava.solidity.types.SBool;

/**
 * Created by gunicolas on 06/02/17.
 */

public class SBoolDecoder implements SDecoder<SBool> {

    @Override
    public SBool decode(String toDecode) {
        boolean value = toDecode.toCharArray()[63] == '1';
        return SBool.fromBoolean(value);
    }
}
