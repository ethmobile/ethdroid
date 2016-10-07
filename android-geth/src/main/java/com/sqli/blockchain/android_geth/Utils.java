package com.sqli.blockchain.android_geth;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by gunicolas on 3/08/16.
 */
public abstract class Utils {


    public static void saveAssetOnStorage(Context context, String assetFilename,String storagePath) throws Exception {
        AssetManager asset = context.getAssets();
        InputStream in = asset.open(assetFilename);
        String filePath = storagePath+"/"+assetFilename;
        new File(filePath).createNewFile();
        OutputStream out = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int read;
        while((read=in.read(buffer)) != -1){
            out.write(buffer,0,read);
        }
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;

    }

}
