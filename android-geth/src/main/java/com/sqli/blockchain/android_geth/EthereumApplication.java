package com.sqli.blockchain.android_geth;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;


/**
 */

public class EthereumApplication extends Application implements EthereumService.EthereumServiceInterface {

    protected EthereumService ethereumService;
    ServiceConnection ethereumServiceConnection;
    Intent ethereumServiceIntent;

    List<EthereumService.EthereumServiceInterface> ethereumServiceReadySubscribers;

    @Override
    public void onCreate() {
        super.onCreate();

        ethereumServiceReadySubscribers = new ArrayList<>();

        ethereumServiceIntent = new Intent(this, EthereumService.class);
        ethereumServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                EthereumService.LocalBinder binder = (EthereumService.LocalBinder) iBinder;
                ethereumService = binder.getServiceInstance();
                ethereumService.registerClient(EthereumApplication.this);
                ethereumService.checkGethReady();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        startService(ethereumServiceIntent);
        bindService(ethereumServiceIntent, ethereumServiceConnection, BIND_AUTO_CREATE);

    }

    @Override
    public void onTerminate() {
        ethereumService.stop();
        ethereumService.stopSelf();
        unbindService(ethereumServiceConnection);
        stopService(ethereumServiceIntent);

        super.onTerminate();
    }

    @Override
    public void onEthereumServiceReady() {
        ethereumService.unregisterClient(this);
        for (EthereumService.EthereumServiceInterface subscriber : ethereumServiceReadySubscribers) {
            subscriber.onEthereumServiceReady();
        }
    }

    public void registerGethReady(EthereumService.EthereumServiceInterface subscriber) {
        ethereumServiceReadySubscribers.add(subscriber);
    }
}
