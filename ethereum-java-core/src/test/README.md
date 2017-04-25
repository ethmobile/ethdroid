# How to test Ethereum-java-core


## Add a smart-contract test function

1. Use https://ethereum.github.io/browser-solidity/ to code your function
2. Copy **Bytecode** and paste it into ```deployContract.js``` script, as value of ```var bytecode```
3. Same thing with **Interface**, as value of ```var abi```
4. Create function Java interface into ```srZc/test/java/ethereumjava/solidity/ITestContract.java```
5. Implement a new JUnit4 testFunction in ```src/test/java/ethereumjava/solidity/ContractText.java```


## Change geth version executing tests
Tests are currently working with a version of geth **under 1.6**

If you want to change the version you can set the binary path into the ```prerequisite.sh```.

Modify the constant value : ```geth_binary_path```

