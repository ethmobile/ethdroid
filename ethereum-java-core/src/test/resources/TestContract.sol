pragma solidity ^0.4.9;

contract TestContract {

    /* Test events */
    event testEventReturnsUInt(uint);
    function throwEventReturnsUInt(){testEventReturnsUInt(2);}

    event testEventReturnsBool(bool);
    function throwEventReturnsBool(){testEventReturnsBool(true);}

    event testEventReturnsMatrix(int[3][3]);
    function throwEventReturnsMatrix(){testEventReturnsMatrix([[int(0),int(1),int(2)],[int(2),int(1),int(1)],[int(2),int(0),int(1)]]);}
    /*-----------------------------*/

    /* Test output types */
    function testFunctionOutputsVoid(){}
    function testFunctionOutputsBool() returns(bool){return true;}
    function testFunctionOutputsPrimitive() returns(uint){
        return 3;
    }
    function testFunctionOutputsMatrix() returns(uint8[3][3]){
        return [[uint8(0),uint8(1),uint8(2)],[uint8(2),uint8(1),uint8(1)],[uint8(2),uint8(0),uint8(1)]];
    }
    function testFunctionOutputs2() returns(bool,bool){
        return(true,false);
    }
    /*-----------------------------*/

    /* Test input types*/
    function testFunctionInputsPrimitives(uint x,uint y){}
    function testFunctionInputsArray(uint8[3] a){}
    /*-----------------------------*/

}
