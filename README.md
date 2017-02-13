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

Futhermore **Rx-java** and its extensions simplify control of asynchronous flows and background processes.

This package can be used on **Android 21+** and with **Geth 1.4+**.

## Installation

1. Start an Android project
2. In you build.gradle, add these dependencies :

    ```
    compile 'com.sqli:ethereum-android:0.1.$VERSION@aar'
    compile 'com.sqli:android-geth:0.1.$VERSION@aar'
    compile 'com.sqli:ethereum-java-core:0.1.$VERSION'
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
observable.subscribe(new Action1<NodeInfo>() {
    @Override
    public void call(NodeInfo nodeInfo) {
        System.out.println(nodeInfo);  
    }
});

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
    }    
    ```

2. Create the related Java interface :
    ```java
    interface ContractExample extends ContractType{

        @SolidityEvent.Parameters({
            @SolidityEvent.Parameter(SBool.class)
        })
        SolidityEvent<SBool> e();

        @SolidityFunction.ReturnType(SVoid.class)
        SolidityFunction<SVoid> foo();

        @SolidityFunction.ReturnType(SUint.SUInt256.class)
        SolidityFunction<SVoid> bar(SInt.SInt256 a);
    }
    ```

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
    .subscribe(new Action1<Transaction>() {
        @Override
        public void call(Transaction s) {
            /*
                Call has been made on the contract
                can verify with the transaction reference and update the android view
            */
        }
    });
```



### Listen to *smart-contract* events

From Android app, subscribe to Solidity events in a background process and be notified in main thread to update the view.

```java
contract.e.watch()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(new Action1<SInt256>() {
        @Override
        public void call(SInt256 s) {
            /*
                you're notified that counter has been incremented 5 more times
                parameter 's' has the current counter's value.
                You can directly update your app view
            */
        }
    });
```

## Project Architecture 

//TODO
## Our architecture vs the world
//TODO
## Contribute
//TODO
## Licence

```
The MIT License (MIT)

Copyright (c) 2015 Damien Lecan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


