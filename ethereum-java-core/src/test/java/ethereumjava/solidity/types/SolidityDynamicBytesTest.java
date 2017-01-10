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
public class SolidityDynamicBytesTest {

    private Object[] parametersForIsType() {
        return new Object[]{
            new Object[]{"bytes", true},
            new Object[]{"bytes[]", true},
            new Object[]{"bytes[4]", true},
            new Object[]{"bytes[][]", true},
            new Object[]{"bytes[3][]", true},
            new Object[]{"bytes[][6][]", true},
            new Object[]{"bytes[3][]", true},
            new Object[]{"bytes32", false},
            new Object[]{"bytes64[]", false},
            new Object[]{"bytes8[4]", false},
            new Object[]{"bytes256[][]", false},
            new Object[]{"bytes64[][6][]", false}
        };
    }

    @Test
    @Parameters
    public void isType(String type, boolean expected) throws Exception {
        final boolean result = SDynamicBytes.isType(type);
        assertEquals(expected, result);
    }
}
