package io.ethmobile.ethdroid.contract;

import io.ethmobile.ethdroid.solidity.ContractType;
import io.ethmobile.ethdroid.solidity.element.SolidityElement;
import io.ethmobile.ethdroid.solidity.element.event.SolidityEvent;
import io.ethmobile.ethdroid.solidity.element.event.SolidityEvent3;
import io.ethmobile.ethdroid.solidity.element.function.SolidityFunction;
import io.ethmobile.ethdroid.solidity.element.function.SolidityFunction2;
import io.ethmobile.ethdroid.solidity.element.function.SolidityFunction3;
import io.ethmobile.ethdroid.solidity.types.SArray;
import io.ethmobile.ethdroid.solidity.types.SBool;
import io.ethmobile.ethdroid.solidity.types.SInt;
import io.ethmobile.ethdroid.solidity.types.SUInt;

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

    @SolidityElement.ReturnParameters({@SArray.Size({3, 3})})
    SolidityEvent<SArray<SArray<SInt.SInt256>>> testEventReturnsMatrix();

    SolidityFunction throwEventReturnsMatrix();

    SolidityEvent3<SBool, SBool, SBool> testEventReturnsMultiple();

    SolidityFunction throwEventReturnsMultiple();

    /*-----------------------------*/

    /*------------------------------*/
    /* Test output types            */
    /*------------------------------*/
    SolidityFunction testFunctionOutputsVoid();

    SolidityFunction<SBool> testFunctionOutputsBool();

    SolidityFunction<SUInt.SUInt256> testFunctionOutputsPrimitive();

    @SolidityElement.ReturnParameters({@SArray.Size({3, 3})})
    SolidityFunction<SArray<SArray<SUInt.SUInt8>>> testFunctionOutputsMatrix();

    SolidityFunction2<SBool, SBool> testFunctionOutputs2();

    @SolidityElement.ReturnParameters({@SArray.Size(), @SArray.Size({2, 3})})
    SolidityFunction3<SBool, SArray<SArray<SUInt.SUInt8>>, SBool> testFunctionOutputs3Matrix();

    @SolidityElement.ReturnParameters({@SArray.Size(), @SArray.Size({3})})
    SolidityFunction2<SBool, SArray<SUInt.SUInt8>> testFunctionThrowsException();
    /*-----------------------------*/

    /*------------------------------*/
    /* Test input types             */
    /*------------------------------*/
    SolidityFunction testFunctionInputsPrimitives(SUInt.SUInt256 x, SUInt.SUInt256 y);

    SolidityFunction testFunctionInputsArray(@SArray.Size({3}) SArray<SUInt.SUInt8> a);
    /*-----------------------------*/

}
