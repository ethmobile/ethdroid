package ethereumjava.solidity;

import ethereumjava.solidity.element.SolidityElement;
import ethereumjava.solidity.element.SolidityEvent;
import ethereumjava.solidity.element.function.SolidityFunction;
import ethereumjava.solidity.element.function.SolidityFunction2;
import ethereumjava.solidity.element.function.SolidityFunction3;
import ethereumjava.solidity.types.SArray;
import ethereumjava.solidity.types.SBool;
import ethereumjava.solidity.types.SInt;
import ethereumjava.solidity.types.SUInt;

/**
 * Created by gunicolas on 08/03/17.
 */

interface ITestContract extends ContractType {

    /*------------------------------*/
    /* Test events                  */
    /*------------------------------*/
    SolidityEvent<SUInt.SUInt256> testEventReturnsUInt();
    SolidityFunction throwEventReturnsUInt();
    SolidityEvent<SBool> testEventReturnsBool();
    SolidityFunction throwEventReturnsBool();
    @SolidityElement.ReturnParameters({@SArray.Size({3,3})})
    SolidityEvent<SArray<SArray<SInt.SInt256>>> testEventReturnsMatrix();
    SolidityFunction throwEventReturnsMatrix();
    /*-----------------------------*/

    /*------------------------------*/
    /* Test output types            */
    /*------------------------------*/
    SolidityFunction testFunctionOutputsVoid();
    SolidityFunction<SBool> testFunctionOutputsBool();
    SolidityFunction<SUInt.SUInt256> testFunctionOutputsPrimitive();
    @SolidityElement.ReturnParameters({@SArray.Size({3,3})})
    SolidityFunction<SArray<SArray<SUInt.SUInt8>>> testFunctionOutputsMatrix();

    SolidityFunction2<SBool,SBool> testFunctionOutputs2();
    @SolidityElement.ReturnParameters({@SArray.Size(),@SArray.Size({2,3})})
    SolidityFunction3<SBool,SArray<SArray<SUInt.SUInt8>>,SBool> testFunctionOutputs3Matrix();
    /*-----------------------------*/

    /*------------------------------*/
    /* Test input types             */
    /*------------------------------*/
    SolidityFunction testFunctionInputsPrimitives(SUInt.SUInt256 x, SUInt.SUInt256 y);
    SolidityFunction testFunctionInputsArray(@SArray.Size({3}) SArray<SUInt.SUInt8> a);
    /*-----------------------------*/

}
