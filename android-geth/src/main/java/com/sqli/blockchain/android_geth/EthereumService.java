package com.sqli.blockchain.android_geth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.ethereum.go_ethereum.cmd.Geth;

/**
 * Created by gunicolas on 26/07/16.
 */
public class EthereumService extends Service {

    public static final String TAG = "ETHEREUM_SERVICE";
    public static final int GETH_NETWORK_ID = 100;
    public static final String GETH_IPC_FILE = "geth.ipc";
    public static final String GETH_GENESIS_FILE = "genesis.json";
    public static final String GETH_BOOTNODE_FILE = "static-nodes.json";

    public static String dataDir;

    private List<EthereumServiceInterface> callbacks;
    private final IBinder mBinder = new LocalBinder();

    private Thread gethThread;
    private Thread checkFileThread;
    private boolean gethReady;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setGethReady(false);

        dataDir = getFilesDir().getAbsolutePath();
        callbacks = new ArrayList<>();

        deleteDatadir();

        try {

            Utils.saveAssetOnStorage(getBaseContext(),GETH_GENESIS_FILE,dataDir);

            final StringBuilder gethParams = new StringBuilder();
            gethParams.append("--fast").append(" ");
            gethParams.append("--lightkdf").append(" ");
            gethParams.append("--nodiscover").append(" ");
            gethParams.append("--networkid "+GETH_NETWORK_ID).append(" ");
            gethParams.append("--datadir=" + dataDir + "/node").append(" ");
            gethParams.append("--genesis=" + dataDir + "/" + GETH_GENESIS_FILE).append(" ");

            runGeth(gethParams.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    private boolean deleteDatadir(){
        File f = new File(dataDir+"/node");
        deleteRecursive(f);
        return true;
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private void runGeth(final String gethParams) {
        gethThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Geth.run( gethParams );
            }
        });
        gethThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public interface EthereumServiceInterface {
        void onEthereumServiceReady();
    }
    public class LocalBinder extends Binder {
        public EthereumService getServiceInstance(){
            return EthereumService.this;
        }
    }

    public synchronized void registerClient(EthereumServiceInterface client){
        if( gethReady ){
            client.onEthereumServiceReady();
        } else {
            this.callbacks.add(client);
        }
    }
    public synchronized void unregisterClient(EthereumServiceInterface client){
        this.callbacks.remove(client);
    }

    public void checkGethReady() {
        checkFileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while( !new File(getIpcFilePath()).exists() ){
                        Thread.sleep(500);
                    }
                    dispatchCallback();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    checkFileThread.interrupt();
                }
            }
        });
        checkFileThread.start();
    }
    private void dispatchCallback(){
        setGethReady(true);
        for(EthereumServiceInterface client : this.callbacks){
            client.onEthereumServiceReady();
        }
    }


    public void stop(){
        gethThread.interrupt();
        if( deleteDatadir() ) {
            Log.d(TAG,"ipc file deleted");
            checkFileThread.interrupt();
        } else Log.e(TAG,"delete ipc file error");

        gethReady = false;
    }

    public String getIpcFilePath(){
        return dataDir + "/node/"+ GETH_IPC_FILE;
    }

    public synchronized void setGethReady(boolean gethReady) {
        this.gethReady = gethReady;
    }
}
