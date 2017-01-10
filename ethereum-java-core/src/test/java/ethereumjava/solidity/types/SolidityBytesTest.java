package ethereumjava.solidity.types;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by gunicolas on 16/08/16.
 */
@RunWith(JUnitParamsRunner.class)
public class SolidityBytesTest {

    private Object[] parametersForIsType() {
        return new Object[]{
            new Object[]{"bytes32", true},
            new Object[]{"bytes64[]", true},
            new Object[]{"bytes8[4]", true},
            new Object[]{"bytes256[][]", true},
            new Object[]{"bytes64[][6][]", true},
            new Object[]{"bytes", false},
            new Object[]{"bytes[]", false},
            new Object[]{"bytes[4]", false},
            new Object[]{"bytes[][]", false},
            new Object[]{"bytes[][6][]", false}
        };
    }

    @Test
    @Parameters
    public void isType(String type, boolean expected) throws Exception {
        final boolean result = SBytes.isType(type);
        assertEquals(expected, result);
    }

}
