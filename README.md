# Ethereum-android

[![Build Status](https://travis-ci.org/sqli-nantes/ethereum-android.svg?branch=master)](https://travis-ci.org/sqli-nantes/ethereum-android)


This repository contains Android-Geth and Ethereum-Android modules.
* Android-Geth allows to easily start Geth service inside your Android app.
* Ethereum-Android is an adaptation of [EthereumJava](https://github.com/sqli-nantes/ethereum-java) in Android context. It allows to communicate with a Geth node via IPC (Android Unix file socket) directly from your Android app.

# Installation
```bash
git clone https://github.com/sqli-nantes/ethereum-android.git
cd ethereum-android
export ANDROID_HOME=/path/to/Android/SDK
./gradlew assemble
```

You can import the generated aars in your android project :

* ./ethereum-android/build/outputs/aar/ethereum-android-dailybuild-***build_number***.aar
* ./android-geth/build/outputs/aar/android-geth-release.aar

# Utilisation

## Android-Geth

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

## Ethereum-android

### Setup EthereumJava 

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

### Call from Activity

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