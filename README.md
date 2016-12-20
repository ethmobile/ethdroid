# Ethereum-android

[![Build Status](https://travis-ci.org/sqli-nantes/ethereum-android.svg?branch=master)](https://travis-ci.org/sqli-nantes/ethereum-android)
[ ![Download Android-geth](https://api.bintray.com/packages/sqli-nantes/ethereum-android/android-geth/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/android-geth/_latestVersion)
[ ![Download Ethereum-android](https://api.bintray.com/packages/sqli-nantes/ethereum-android/ethereum-android/images/download.svg) ](https://bintray.com/sqli-nantes/ethereum-android/ethereum-android/_latestVersion)

This repository contains Android-Geth and Ethereum-Android modules.
* Android-Geth allows to easily start Geth service inside your Android app.
* Ethereum-Android is an adaptation of [EthereumJava](https://github.com/sqli-nantes/ethereum-java) in Android context. It allows to communicate with a Geth node via IPC (Android Unix file socket) directly from your Android app.

# Installation

## Android Geth

In your app build.gradle

```java 

dependencies {
    [...]
    compile 'com.sqli:ethereum-android:0.1.20161219'
}

repositories {
    maven {
        url "http://dl.bintray.com/sqli-nantes/ethereum-android"
    }
}
```

:warning: **0.1.20161219** is a version of android-geth. You can specify another one listed in android-geth repository. Latest one is quoted at the top of this file.

**See** [android-geth repository](https://bintray.com/sqli-nantes/ethereum-android/android-geth)

## Ethereum-Android

In your app build.gradle

```java 

dependencies {
    [...]
    compile 'com.sqli:ethereum-android:0.1.20161219'
}

repositories {
    maven {
        url "http://dl.bintray.com/sqli-nantes/ethereum-android"
    }
    maven {
        url "http://dl.bintray.com/sqli-nantes/ethereum-java"
    }
}
```

:warning: **0.1.20161219** is a version of ethereum-android. You can specify another one listed in ethereum-android repository. Latest one is quoted at the top of this file.

**Ethereum-android** is a library which extends **Ethereum-java-core** library.
Both maven repositories are required because of they are unofficial. 

**See** [ethereum-android repository](https://bintray.com/sqli-nantes/ethereum-android/ethereum-java)

**See** [ethereum-java-core repository](https://bintray.com/sqli-nantes/ethereum-java/ethereum-java-core)

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
