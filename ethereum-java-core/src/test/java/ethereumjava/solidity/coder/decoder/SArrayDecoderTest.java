package ethereumjava.solidity.coder.decoder;

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
 * Created by gunicolas on 08/02/17.
 */
@RunWith(JUnitParamsRunner.class)
public class SArrayDecoderTest {

    private Object[] parametersForTestDecoderSArray(){
        return new Object[]{
            // bool[2]      [true,false]
            new Object[]{   "0000000000000000000000000000000000000000000000000000000000000001" +
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            SArray.fromArray(new SBool[]{SBool.fromBoolean(true),SBool.fromBoolean(false)})
                        },
            // bool[]       [true,true,false]
            new Object[]{   "0000000000000000000000000000000000000000000000000000000000000020" +
                            "0000000000000000000000000000000000000000000000000000000000000003" +
                            "0000000000000000000000000000000000000000000000000000000000000001" +
                            "0000000000000000000000000000000000000000000000000000000000000001" +
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            SArray.fromList(new ArrayList<SBool>(){{
                                add(SBool.fromBoolean(true));
                                add(SBool.fromBoolean(true));
                                add(SBool.fromBoolean(false));
                            }})
            },
            // bool[1][2]       [[false],[false]]
            new Object[]{   "0000000000000000000000000000000000000000000000000000000000000000" +
                            "0000000000000000000000000000000000000000000000000000000000000000",
                            SArray.fromArray(new SArray[]{
                                SArray.fromArray(new SBool[]{SBool.fromBoolean(false)}),
                                SArray.fromArray(new SBool[]{SBool.fromBoolean(false)})
                            }),
            }

        };
    }

    @org.junit.Test
    @Parameters
    public void testDecoderSArray(String value,SArray<? extends SType> expected) throws Exception {
        try {
            SArray result = SCoder.decodeParam(value,SArray.class);
            Assert.assertTrue(result.equals(expected));
        } catch( EthereumJavaException e){
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

}
