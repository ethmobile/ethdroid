package ethereumjava;

import org.junit.Assert;
import org.junit.Test;

import ethereumjava.solidity.element.function.SolidityFunction;
import ethereumjava.solidity.element.function.SolidityFunction2;
import ethereumjava.solidity.types.SUInt;

/**
 * Created by gunicolas on 22/03/17.
 */

public class UtilsTest {

    @Test
    public void isClassOrSubclassTest() throws Exception{
        boolean got = Utils.isClassOrSubclass(SolidityFunction2.class, SolidityFunction.class);
        Assert.assertTrue(got);

        got = Utils.isClassOrSubclass(SolidityFunction.class, SolidityFunction.class);
        Assert.assertTrue(got);

        got = Utils.isClassOrSubclass(SUInt.class, SolidityFunction.class);
        Assert.assertFalse(got);


    }
}
