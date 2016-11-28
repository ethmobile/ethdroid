package com.sqli.blockchain.ethereum_android_sample.contract;

import ethereumjava.solidity.ContractType;
import ethereumjava.solidity.SolidityFunction;
import ethereumjava.solidity.types.SVoid;

/**
 * Created by root on 15/11/16.
 */

public interface ContractSample extends ContractType {

    SolidityFunction<SVoid> increase();
}
