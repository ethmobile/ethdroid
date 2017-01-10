package ethereumjava.solidity.types;

import junit.framework.TestCase;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by gunicolas on 07/09/16.
 */
@RunWith(JUnitParamsRunner.class)
public class SAddressTest {

    private Object[] parametersForIsType() {
        return new Object[]{
            new Object[]{"address", true},
            new Object[]{"address[]", true},
            new Object[]{"address[4]", true},
            new Object[]{"address[][]", true},
            new Object[]{"address[3][]", true},
            new Object[]{"address[][6][]", true},
            new Object[]{"uint[][6][]", false},
            new Object[]{"address[]1", false},
            new Object[]{"addresS", false}
        };
    }

    @Test
    @Parameters
    public void isType(String type, boolean expected) {
        final boolean result = SAddress.isType(type);
        assertEquals(expected, result);
    }


    private Object[] parametersForTestIsAddress() {
        return new Object[]{
            new Object[]{"0x56994a8fda2c2dd20761cc2e5efecf22B2936603", true},
            new Object[]{"0x56994A8FDA2C2DD20761CC2E5EFECF22B2936603", true},
            new Object[]{"56994a8fda2c2dd20761cc2e5efecf22B2936603", true},
            new Object[]{"56994A8FDA2C2DD20761CC2E5EFECF22B2936603", true},
            new Object[]{"56994A8FDA2Z2DD20761CC2E5EFECF22B2936603", false},
            new Object[]{"0x56994A8FDA2C2DD20761CC2E5EFECF22B293660354ZZZZZ3", false},
            new Object[]{"56994A8FDA2C2DD20761CCF22B2936603", false}
        };
    }

    @Test
    @Parameters
    public void testIsAddress(String address, boolean expected) {
        final boolean result = SAddress.isAddress(address);
        TestCase.assertEquals(expected, result);
    }

}
