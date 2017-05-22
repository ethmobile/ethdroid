package com.sqli.blockchain.ethdroid_sample;

/**
 * Created by gunicolas on 19/05/17.
 */

public class Log {

    private static final String LOG_TAG = "ETHDROID_SAMPLE";

    public static void print(String message){
        android.util.Log.d(LOG_TAG,message);
    }
}
