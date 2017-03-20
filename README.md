# Ethereum-android : Easy-to-use Ethereum client wrapper for Android 

[![Build Status](https://travis-ci.org/sqli-nantes/ethereum-android.svg?branch=master)](https://travis-ci.org/sqli-nantes/ethereum-android)
[ ![Download Android-geth](https://api.bintray.com/packages/sqli-nantes/ethereum-android/android-geth/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/android-geth/_latestVersion)
[ ![Download Ethereum-android](https://api.bintray.com/packages/sqli-nantes/ethereum-android/ethereum-android/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/ethereum-android/_latestVersion)
[ ![Download Ethereum-java-core](https://api.bintray.com/packages/sqli-nantes/ethereum-android/ethereum-java-core/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/ethereum-java-core/_latestVersion)

## Why using Ethereum-Android

If you think that smartphone is future of blockchain, **Ethereum-android** is here to help you building amazing decentralized smartphone apps. 

This project was born as soon as the Geth community built the first android archive. Our needs was to allow an Android App communicate with an inproc Geth node through IPC and RPC. 

Thanks to our hard work, we've reached our goals and built 3 differents use-cases of decentralized smartphone apps : 
* decentralized car sharing
* voting through blockchain
* location of peoples all arround the world

Contact us for more informations on our works.

With Ethereum-android it becomes easier to :
* instanciate an Ethereum go-ethereum inproc node, 
* manage accounts, 
* get nodes information, 
* send Ether 
* and also call smart-contracts. 

Futhermore **Rx-java 1** and its extensions simplify control of asynchronous flows and background processes.

This package can be used on **Android 21+** and with **Geth 1.4+**.

## Installation

1. Start an Android project
2. In you build.gradle, add these dependencies :

    ```
    compile 'com.sqli:ethereum-android:0.1.20170320@aar'
    compile 'com.sqli:android-geth:0.1.20170320@aar'
    compile 'com.sqli:ethereum-java-core:0.1.20170320'
    compile 'ethereumandroid:geth:1.5.0-unstable'
    ```
    and these related repositories :
    ```
    repositories {
        maven { url "http://dl.bintray.com/sqli-nantes/ethereum-android"}
        maven { url "http://dl.bintray.com/androidgeth/Geth" }
    }
    ```

3. Put the *genesis.json* of your private blockchain in the *src/main/assets* folder
4. Add these permissions in *AndroidManifest.xml* :

    ```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    ```
4. Rebuild project to download dependencies
5. Start building awesome apps.

## Getting Started
### Start an inproc node

Create your Application subclass of EthereumApplication

:warning: don't forget to declare the Application in *AndroidManifest.xml*
```java
import com.sqli.blockchain.android_geth.EthereumApplication;

public class MyApplication extends EthereumApplication{
    [...]
}
```

In each activity you can be notified when Geth service is available, registering to the event in onCreate method.

When onEthereumServiceReady is called, you can do everything you want, like update UI, or create instanciate EthereumJava (see next section).
```java
import com.sqli.blockchain.android_geth.EthereumService;

public class MainActivity implements EthereumService.EthereumServiceInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        [...]
        MyApplication application = (MyApplication) getApplication();
        application.registerGethReady(this);
    }

    @Override
    public void onEthereumServiceReady() {
        //Update UI to start comunication
        super.onEthereumServiceReady();
    }
}
```

### Communicate with a node

In your Application class :
```java
[...]
public EthereumJava ethereumjava;
[...]

public EthereumJava getEthereumJava() {
    return ethereumjava;
}

@Override
public void onEthereumServiceReady() {
    ethereumjava = new EthereumJava.Builder()
            .provider(new AndroidIpcProvider(ethereumService.getIpcFilePath()))
            .build();

    super.onEthereumServiceReady();
}  
```

### Accounts management

```java
String password = "passwd";
String accountId = ethereumJava.personal.createAccount(password);
boolean unlocked = ethereumJava.personal.unlockAccount(accountId);
```

### Call a node module
**Example :** get node information asynchronously 
```java
EthereumJava ethereumJava = ((MyApplication) getApplication()).getEthereumJava()
Observable<NodeInfo> observable = ethereumJava.admin.getNodeInfo()
observable.subscribe(nodeInfo -> System.out.println(nodeInfo));

```


### Send 1 Ether

```java
String from = "0xaaaa...";
String to = "0xbbbb...";

TransactionRequest tr = new TransactionRequest("","");
tr.setValueHex(SolidityUtils.toWei(BigInteger.ONE.toString(),"ether").toBigInteger());        
Hash transactionHash = ethereumJava.eth.sendTransaction(tr);

```



### Instanciate a *smart-contract* 

1. From the following Solidity smart-contract source code:
    ```javascript
    contract ContractExample {

        event e(bool);

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
    }    
    ```

2. Create the related Java interface :
    ```java
    interface ContractExample extends ContractType{

        SolidityEvent<SBool> e();
        SolidityFunction foo();
        SolidityFunction bar(SInt.SInt256 a);
     
       @SolidityElement.ReturnParameters({@SArray.Size({3,3})}) 
       SolidityEvent<SArray<SArray<SInt.SInt256>>> eventOutputMatrix();
 
       @SolidityElement.ReturnParameters({@SArray.Size({2,8})})
       SolidityFunction<SArray<SArray<SUInt.SUInt8>>> functionOutputMatrix();
  
       SolidityFunction functionInputMatrix(@SArray.Size({3}) SArray<SUInt.SUInt8> a);
    }
    ```
    
    When an array/matrix is returned by a SolidityElement (function/event), you have to specify its 
    size with the **@SolidityElement.ReturnParameters** annotation. The value is an array of **@SArray.Size** 
    annotations. This annotation takes an array of integer sizes like *int[2][3][4] -> {2,3,4}*.

3. Instanciate the *smart-contract* available on the blockchain at the given address

    ```java
    ContractExample contract = ethereumJava.contract.withAbi(ContractExample.class).at("0xcccc...");
    ```

### Interact with deployed *smart-contract*

#### Call a *smart-contract* function and be notified when it's mined

```java
String from = "0xaaaa..."; /* put here the account which call the function */
contract.foo().sendTransactionAndGetMined(from,new BigInteger("90000"))
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(minedTransaction -> doWhenMined()); 
    /*
        doWhenMined is a local method called when transaction has been mined.
        You can refresh the view directly in this method
    */
```

### Listen to *smart-contract* events

From Android app, subscribe to Solidity events in a background process and be notified in main thread to update the view.

```java
contract.e.watch()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(booleanAnswer -> doWhenEventTriggered());
    /*
        doWhenEvent is a local method called when event is triggered by the deployed smart-contract
        booleanAnswer is the parameter returned by the triggered event. 
        You can directly update your app view
    */
```

## Project Architecture 
//TODO
## Contribute
//TODO
## Licence

```
The MIT License (MIT)
```
See [Licence](LICENSE).

