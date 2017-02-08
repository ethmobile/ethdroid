package ethereumjava.solidity.coder.encoder;

import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.solidity.coder.SCoder;
import ethereumjava.solidity.types.SArray;
import ethereumjava.solidity.types.SBool;
import ethereumjava.solidity.types.SType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Created by gunicolas on 07/02/17.
 */
@RunWith(JUnitParamsRunner.class)
public class SArrayEncoderTest {

    private Object[] parametersForTestEncoderSArray(){
        return new Object[]{
            // bool[2]      [true,false]
            new Object[]{   SArray.fromArray(new SBool[]{SBool.fromBoolean(true),SBool.fromBoolean(false)}),
                            "0000000000000000000000000000000000000000000000000000000000000001" +
                            "0000000000000000000000000000000000000000000000000000000000000000"
                        },
            // bool[]       [true,true,false]
            new Object[]{   SArray.fromList(new ArrayList<SBool>(){{
                                add(SBool.fromBoolean(true));
                                add(SBool.fromBoolean(true));
                                add(SBool.fromBoolean(false));
                            }}),
                            "0000000000000000000000000000000000000000000000000000000000000020" +
                            "0000000000000000000000000000000000000000000000000000000000000003" +
                            "0000000000000000000000000000000000000000000000000000000000000001" +
                            "0000000000000000000000000000000000000000000000000000000000000001" +
                            "0000000000000000000000000000000000000000000000000000000000000000"
                        },
            // bool[1][2]       [[false],[false]]
            new Object[]{   SArray.fromArray(new SArray[]{
                                SArray.fromArray(new SBool[]{SBool.fromBoolean(false)}),
                                SArray.fromArray(new SBool[]{SBool.fromBoolean(false)})
                            }),
                            "0000000000000000000000000000000000000000000000000000000000000000" +
                            "0000000000000000000000000000000000000000000000000000000000000000"
                        }

        };
    }

    @org.junit.Test
    @Parameters
    public void testEncoderSArray(SArray<? extends SType> value,String expected) throws Exception {

        try {
            String got = SCoder.encodeParam(value);
            Assert.assertTrue(got.compareTo(expected) == 0);
        } catch( EthereumJavaException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

}
