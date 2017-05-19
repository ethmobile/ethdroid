package com.sqli.blockchain.ethdroid.solidity.coder.encoder;

import com.sqli.blockchain.ethdroid.solidity.SolidityUtils;
import com.sqli.blockchain.ethdroid.solidity.types.SUInt;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


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
