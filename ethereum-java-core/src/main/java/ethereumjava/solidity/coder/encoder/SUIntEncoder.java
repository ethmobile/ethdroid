package ethereumjava.solidity.coder.encoder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.types.SUInt;


/**
 * Created by gunicolas on 05/10/16.
 */

public class SUIntEncoder implements SEncoder<SUInt> {


    @Override
    public String encode(SUInt toEncode) {
        BigDecimal twoCompDec = SolidityUtils.toTwosComplement(toEncode.asString());
        MathContext mc = new MathContext(0, RoundingMode.DOWN);
        BigDecimal twoCompDecRounded = twoCompDec.round(mc);
        String twoCompDecRoundedHexa = SolidityUtils.bigDecimalToHexString(twoCompDecRounded);
        String result = SolidityUtils.padLeftWithZeros(twoCompDecRoundedHexa, 64);
        return result;
    }
}
