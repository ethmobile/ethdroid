package com.sqli.blockchain.android_geth;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import ethereumjava.EthereumJava;
import web3j.net.provider.AndroidIpcProvider;


/**
 * Created by gunicolas on 22/08/16.
 */
public abstract class EthereumActivity extends AppCompatActivity implements EthereumService.EthereumServiceInterface {

    static final String TAG = EthereumActivity.class.getSimpleName();

    EthereumService ethereumService;
    ServiceConnection ethereumServiceConnection;
    Intent ethereumServiceIntent;

    protected EthereumJava web3J;


    @Override
    protected void onResume() {
        super.onResume();

        ethereumServiceIntent = new Intent(this,EthereumService.class);
        ethereumServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                EthereumService.LocalBinder binder = (EthereumService.LocalBinder) iBinder;
                ethereumService = binder.getServiceInstance();
                ethereumService.registerClient(EthereumActivity.this);
                ethereumService.checkGethReady();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        startService(ethereumServiceIntent);
        bindService(ethereumServiceIntent,ethereumServiceConnection,BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        ethereumService.stop();
        ethereumService.stopSelf();
        unbindService(ethereumServiceConnection);
        stopService(ethereumServiceIntent);

        super.onStop();
    }

    @Override
    public void onEthereumServiceReady() {
        ethereumService.unregisterClient(this);
        String dir = ethereumService.getIpcFilePath();
        AndroidIpcProvider provider = new AndroidIpcProvider(dir);
        provider.init();
        provider.startListening();
        EthereumJava.Builder builder = new EthereumJava.Builder();
        builder.provider(provider);
        web3J = builder.build();
    }




}
