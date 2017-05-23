package com.sqli.blockchain.ethdroid_sample;

import com.sqli.blockchain.ethdroid.solidity.ContractType;
import com.sqli.blockchain.ethdroid.solidity.element.SolidityElement;
import com.sqli.blockchain.ethdroid.solidity.element.event.SolidityEvent;
import com.sqli.blockchain.ethdroid.solidity.element.event.SolidityEvent3;
import com.sqli.blockchain.ethdroid.solidity.element.function.SolidityFunction;
import com.sqli.blockchain.ethdroid.solidity.element.function.SolidityFunction2;
import com.sqli.blockchain.ethdroid.solidity.element.function.SolidityFunction3;
import com.sqli.blockchain.ethdroid.solidity.types.SArray;
import com.sqli.blockchain.ethdroid.solidity.types.SBool;
import com.sqli.blockchain.ethdroid.solidity.types.SInt;
import com.sqli.blockchain.ethdroid.solidity.types.SUInt;

/**
 * Created by gunicolas on 08/03/17.
 */

interface ITestContract extends ContractType {

    SolidityEvent<SUInt.SUInt8> simpleEvent();

    SolidityFunction<SBool> foo();
    SolidityFunction<SUInt.SUInt8> value();
    SolidityFunction<SUInt.SUInt8> bar(SUInt.SUInt8 a);

    SolidityFunction throwEvent();


    /*
    pragma solidity ^0.4.10;
    contract Simple {
        uint8 public value;
        event simpleEvent(uint8);
        function foo() returns(bool){
            return true;
        }
        function bar(uint8 a) returns(uint8){
            value = a;
            return value;
        }
        function throwEvent() {
            simpleEvent(value);
        }
    }
    */

}
