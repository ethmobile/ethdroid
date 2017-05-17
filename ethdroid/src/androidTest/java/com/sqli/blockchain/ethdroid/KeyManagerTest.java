package com.sqli.blockchain.ethdroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by gunicolas on 16/05/17.
 */
public class KeyManagerTest {

    private Context appContext;
    private String datadir;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        datadir = appContext.getFilesDir().getAbsolutePath();
        File keystoreDirectory = new File(datadir+"/keystore");
        if( keystoreDirectory.exists() ) keystoreDirectory.delete();
    }

    @Test
    public void newKeyManagerTest() throws Exception {
        KeyManager.newKeyManager(datadir);
        File dir = new File(datadir+"/keystore");
        Assert.assertTrue(dir.exists() && dir.isDirectory() && dir.list().length==0);
    }


}
