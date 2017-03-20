package com.sqli.blockchain.ethereum_android_sample.contract;

import ethereumjava.solidity.ContractType;
import ethereumjava.solidity.SolidityFunction;
import ethereumjava.solidity.types.SVoid;

/**
 */

public interface ContractSample extends ContractType {

    SolidityFunction increase();
}
