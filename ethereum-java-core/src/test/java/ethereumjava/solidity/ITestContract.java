package ethereumjava.solidity;

import ethereumjava.solidity.types.SArray;
import ethereumjava.solidity.types.SBool;
import ethereumjava.solidity.types.SInt;
import ethereumjava.solidity.types.SUInt;
import ethereumjava.solidity.types.SVoid;

/**
 * Created by gunicolas on 08/03/17.
 */

interface ITestContract extends ContractType {

    /*------------------------------*/
    /* Test events                  */
    /*------------------------------*/
    @SolidityEvent.Parameters({
        @SolidityEvent.Parameter(SUInt.SUInt256.class)
    })
    @SolidityElement.ReturnType("uint")
    SolidityEvent<SUInt.SUInt256> testEventReturnsUInt();
    @SolidityFunction.ReturnType(SVoid.class)
    SolidityFunction<SVoid> throwEventReturnsUInt();

    @SolidityEvent.Parameters({
        @SolidityEvent.Parameter(SBool.class)
    })
    SolidityEvent<SBool> testEventReturnsBool();
    @SolidityFunction.ReturnType(SVoid.class)
    SolidityFunction<SVoid> throwEventReturnsBool();

    @SolidityElement.ReturnType("int[3][3]")
    SolidityEvent<SArray<SArray<SInt.SInt256>>> testEventReturnsMatrix();
    @SolidityFunction.ReturnType(SVoid.class)
    SolidityFunction<SVoid> throwEventReturnsMatrix();
    /*-----------------------------*/


    /*------------------------------*/
    /* Test output types            */
    /*------------------------------*/
    @SolidityFunction.ReturnType(SVoid.class)
    SolidityFunction<SVoid> testFunctionOutputsVoid();

    @SolidityFunction.ReturnType(SBool.class)
    SolidityFunction<SBool> testFunctionOutputsBool();

    @SolidityFunction.ReturnType(SUInt.SUInt256.class)
    SolidityFunction<SUInt.SUInt256> testFunctionOutputsPrimitive();
    /*-----------------------------*/


    /*------------------------------*/
    /* Test input types             */
    /*------------------------------*/
    @SolidityFunction.ReturnType(SVoid.class)
    SolidityFunction<SVoid> testFunctionInputsPrimitives(SUInt.SUInt256 x, SUInt.SUInt256 y);

    @SolidityFunction.ReturnType(SVoid.class)
    SolidityFunction<SVoid> testFunctionInputsArray(@SArray.Type("uint8[3]") SArray<SUInt.SUInt8> a);
    /*-----------------------------*/

}
