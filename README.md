# Ethdroid : Easy-to-use Ethereum Geth wrapper for Android 

    ⚠️ This project isn't maintained anymore, this is why it is read-only.
    If you are interested to work on it, feel free to contact us at 
    ethereummobile@gmail.com, we will be happy to help you.

[![Build Status](https://travis-ci.org/ethmobile/ethdroid.svg?branch=master)](https://travis-ci.org/ethmobile/ethdroid)
[![Coverity Status](https://scan.coverity.com/projects/12878/badge.svg)](https://scan.coverity.com/projects/ethdroid)


## Why using Ethdroid

If you think that smartphone is future of blockchain, **Ethdroid** is here to help you building amazing decentralized smartphone apps. 

This project was born as soon as the Geth community built the first android archive. Our needs was 
to allow an Android App communicate with an inproc Geth node.

Thanks to our hard work, we've reached our goals and built 3 differents use-cases of decentralized smartphone apps : 
* decentralized car sharing
* voting through blockchain
* location of peoples all arround the world

[Contact us](mailto:ethereummobile@gmail.com) for more informations on our works.

With Ethereum-android it becomes easier to :
* instanciate an Ethereum go-ethereum inproc node, 
* manage accounts, 
* get nodes information, 
* send Ether 
* and also call smart-contracts. 

Futhermore **Rx-java 1** and its extensions simplify control of asynchronous flows and background processes.

This package can be used on **Android 22+** and with **Geth 1.6.2+**.

## Download Ethdroid and set Geth version

Ethdroid and Geth ***stable*** versions are distribued throught ***jCenter***.

Because Geth project evolves independently of Ethdroid library. That's why you are free to set the 
latest compatible Geth version as dependency.

```Kotlin
repositories {
    jCenter()
}
dependencies {
    compile 'io.ethmobile:ethdroid:2.0.0-m2'
    compile 'org.ethereum:geth:1.6.5'
}
```

For current ***develop*** version of Ethdroid, you can download via : 

```Kotlin
repositories {
    jCenter()
    maven { url "https://oss.jfrog.org/artifactory/oss-snapshot-local" }
}
dependencies {
    compile 'io.ethmobile:ethdroid:2.0.0-m3-SNAPSHOT'
    compile 'org.ethereum:geth:1.6.5'
}
```


## Getting Started
### Start a Geth node 

With Ethdroid you can easly connect a smartphone to an Ethereum blockchain. 

You can connect to :
* Main network
* test network
* private network

Thanks to the Ethdroid.Builder class, you can customize your node. 

First of all, to create a Builder you have to provide the directory path where blockchain data files will be saved. 

Then, provide the chain configuration, or use the default ones. 

*Optionally*, reference a *Key Manager* if you have already built it.
It's mandatory in order to use account related node functions, but you can reference it later.

*Optionally*, reference a Go Context to use as default when making calls to node (See [golang doc](https://golang.org/pkg/context/)). Default is *backgroundContext*.

Finally, build to get the node reference and start communication with it.


#### on **main network**

```java

new EthDroid.Builder(datadir)  //Whatever directory path where blockchain files must be saved.
    .onMainnet()
    .build()
    .start();

```

#### on **test network**


```java

new EthDroid.Builder(datadir)  //Whatever directory path where blockchain files must be saved.
    .onTestnet()
    .build()
    .start();

```

#### on **private network**

Private network is a little bit trickier because you have to provide more information.

* the genesis : String (standard genesis json file)
* the network id : long (you can find it in your genesis)
* peer node url : String (at least one. It can be a [bootnode](https://github.com/ethereum/go-ethereum/blob/master/cmd/bootnode/main.go) or any other node of the private network. Format is : "enode://*${id}*@*${ip}*:*${port}*?discport:*${port}+1*)

You can find an example at [EthdroidBuilderTest](https://github.com/ethmobile/ethdroid/blob/master/ethdroid/src/androidTest/java/io/ethmobile/ethdroid/EthDroidBuilderTest.java#L48)

```java

EthDroid ethdroid = EthDroid.Builder(datadir) //Whatever directory path where blockchain files must be saved.
    .withChainConfig(new ChainConfig.Builder(networkID, genesis, bootnode).build())
    .build()
    .start();

```


### Accounts management

//TODO

### Send Ether

//TODO

### Interact with a *smart-contract* 

#### Instanciate the *smart-contract* interface

From the following Solidity smart-contract source code:

```javascript
    contract ContractExample {

        event e(bool);
        event multipleEvent(bool,bool,bool);

        void foo(){
            [...]
        }

        uint bar(int a){
            [...]
        }
        
        event eventOutputMatrix(int[3][3]){
            [...]
        }
        
        function functionOutputMatrix() returns(uint8[2][8]){
            [...]
        }
        
        function functionInputMatrix(uint8[3]){
            [...]
        }
        
        function functionOutputMultiple() returns(bool,uint8[2][3]){
            [...]
        }
    }    
```

1. Create the related Java interface :
   
```java
    interface ContractExample extends ContractType{
       
       SolidityEvent<SBool> e();
       
       SolidityEvent3<SBool,SBool,SBool> multipleEvent();
          
       SolidityFunction foo();
       
       SolidityFunction bar(SInt.SInt256 a);
     
  
       @SolidityElement.ReturnParameters({@SArray.Size({3,3})}) 
       SolidityEvent<SArray<SArray<SInt.SInt256>>> eventOutputMatrix();
       
       @SolidityElement.ReturnParameters({@SArray.Size({2,8})})
       SolidityFunction<SArray<SArray<SUInt.SUInt8>>> functionOutputMatrix();
      
       SolidityFunction functionInputMatrix(@SArray.Size({3}) SArray<SUInt.SUInt8> a);
       
       @SolidityElement.ReturnParameters({@SArray.Size(),@SArray.Size({2,3})})
       SolidityFunction2<SBool,SArray<SArray<SUInt.SUInt8>>> functionOutputMultiple();
       
    }
```
    
When an input/output parameter of a function/event is an array, you have to specify its size with 
the annotation : **@SArray.Size**. 

Its parameter is an array of integers like **@SArray.Size{2,3,4}** (its equivalent to an array
of a size *type*[2][3][4])

When your function/event returns an array, to specify its length you have to use the annotation 
**@SolidityElement.ReturnParameters({})** which takes an array of @SArray.Size annotations as value.

When your function returns multiple types, you have to specify the related SolidityFunction.
For exemple, if your function returns 2 booleans you must use return **SolidityFunction2<SBool,SBool>**

Table of multiple return type elements and their related wrapper :

|Number of Returns  |Function                       |Event                      |Output                     |
|--:                |---                            |---                        |---                        |
|0                  |SolidityFunction               |SolidityEvent              |SType                      |
|1                  |SolidityFunction`<T>`          |SolidityEvent`<T>`         |SingleReturn`<T>`          |
|2                  |SolidityFunction2`<T1,T2>`     |SolidityEvent2`<T1,T2>`    |PairReturn`<T1,T2>`        |
|3                  |SolidityFunction3`<T1,T2,T3>`  |SolidityEvent3`<T1,T2,T3>` |TripleReturn`<T1,T2,T3>`   |
    
2. Instanciate the *smart-contract* available on the blockchain at the given address

    //TODO

#### Make a local then reverted call to a *smart-contract* function 

```java

    PairReturn<SBool,SArray<SArray<SUInt.SUInt8>>> result = contract.functionOutputMultiple().call();
    
    SBool resultSBool = result.getElement1();
    
    SArray<SArray<SUInt.SUInt8>> resultMatrix = result.getElement2();

```

#### Make a persistent call to a *smart-contract* function and be notified when it's mined

    //TODO

#### Listen to *smart-contract* events

From Android app, subscribe to Solidity events in a background process and be notified in main thread to update the view.

```java
    contract.e.listen()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(booleanAnswer -> doWhenEventTriggered());
    
    /*
        doWhenEventTriggered is a local method called when event is triggered by the deployed smart-contract
        booleanAnswer is the parameter returned by the triggered event. 
        You can directly update your app view
    */
```

## Project Architecture 

//TODO

## Contribute
//TODO

### Add code style to Android Studio

* For IntelliJ 13+ :

    1. Edit -> Macros -> Start Macro Recording
    2. Code -> Reformat Code
    3. File -> Save all
    4. Edit -> Macros -> Stop Macro Recording
    5. Name the macro (something like "formatted save")
    6. File -> Settings -> Keymap
    7. Right click on the macro. Add Keyboard Shortcut. Set the keyboard shortcut to Control + S. 
    8. IntelliJ will inform you of a hotkey conflict. Select "remove" to remove other assignments.

* For previous versions [click here](https://stackoverflow.com/a/5581992)

## Authors and contributors

- Guillaume Nicolas
- Damien Lecan
- Kevin Biger
- Alice Le Bars
- Mickael Came

## Licencing

Ethdroid is released under the MIT License (MIT), see [Licence](LICENSE).

Ethdroid depends on libraries with different licenses:
- Geth: LGPL-3.0
- RxJava : Apache 2.0
- Google Gson: Apache 2.0
- Squareup Okio: Apache 2.0

Please respect terms and conditions of each licenses.

