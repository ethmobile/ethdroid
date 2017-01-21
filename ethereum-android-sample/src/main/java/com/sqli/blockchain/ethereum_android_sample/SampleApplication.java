package com.sqli.blockchain.ethereum_android_sample;

import com.sqli.blockchain.android_geth.EthereumApplication;

import ethereumjava.EthereumJava;
import ethereumjava.net.provider.AndroidIpcProvider;

/**
 */

public class SampleApplication extends EthereumApplication {

    public EthereumJava ethereumjava;


    @Override
    public void onEthereumServiceReady() {

        String dir = ethereumService.getIpcFilePath();
        ethereumjava = new EthereumJava.Builder()
            .provider(new AndroidIpcProvider(dir))
            .build();

        super.onEthereumServiceReady();

    }

}
