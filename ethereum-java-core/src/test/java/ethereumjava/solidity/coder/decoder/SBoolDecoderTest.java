package ethereumjava.solidity.coder.decoder;

import org.junit.Assert;
import org.junit.runner.RunWith;

import ethereumjava.solidity.coder.SCoder;
import ethereumjava.solidity.types.SBool;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Created by gunicolas on 07/02/17.
 */
@RunWith(JUnitParamsRunner.class)
public class SBoolDecoderTest {

    private Object[] parametersForTestDecoderSBool(){
        return new Object[]{
            new Object[]{"0000000000000000000000000000000000000000000000000000000000000001",true},
            new Object[]{"0000000000000000000000000000000000000000000000000000000000000000",false}
        };
    }

    @org.junit.Test
    @Parameters
    public void testDecoderSBool(String value,boolean expected) throws Exception {

        SBool bool = SCoder.decodeParam(value, SBool.class);
        Assert.assertTrue(expected == bool.get());

    }


}
