# Ethereum-android : Easy-to-use Ethereum client wrapper for Android 

[![Build Status](https://travis-ci.org/sqli-nantes/ethereum-android.svg?branch=master)](https://travis-ci.org/sqli-nantes/ethereum-android)
[ ![Download Android-geth](https://api.bintray.com/packages/sqli-nantes/ethereum-android/android-geth/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/android-geth/_latestVersion)
[ ![Download Ethereum-android](https://api.bintray.com/packages/sqli-nantes/ethereum-android/ethereum-android/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/ethereum-android/_latestVersion)

## Motivations
## Installation
## Getting Started
### Start an inproc node

Create your Application subclass of EthereumApplication
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

### Connect to a node

In your Application class for example :
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
            .provider(new AndroidIpcProvider(ethereumService.getIpcFilePath();))
            .build();

    super.onEthereumServiceReady();
}  
```

### Call a node module
Example : get NodeInfo asynchronously 
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


### Send a transaction
### Interact with deployed *smart-contract*
### Listen to *smart-contract* events

## Project Architecture 
## Our architecture vs the world
## Contribute
## Licence
