package ethereumjava.solidity.coder.encoder;


import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.types.SBytes;

/**
 * Created by gunicolas on 05/10/16.
 */

public class SBytesEncoder implements SEncoder<SBytes> {

    @Override
    public String encode(SBytes toEncode) {
        return SolidityUtils.padRightWithZeros(toEncode.asString(), 64);
    }
}
