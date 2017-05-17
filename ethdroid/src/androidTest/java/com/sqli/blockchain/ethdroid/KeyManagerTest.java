package com.sqli.blockchain.ethdroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sqli.blockchain.ethdroid.sha3.Sha3;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gunicolas on 16/05/17.
 */
public class KeyManagerTest {


    private Context appContext;
    private String datadir;
    private KeyManager keyManager;
    private Account account;
    private static final String PASSWORD = "password";

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        datadir = appContext.getFilesDir().getAbsolutePath();
        File keystoreDirectory = new File(datadir+"/keystore");
        if( keystoreDirectory.exists() ) deleteDir(keystoreDirectory);
    }

    @Test
    public void newKeyManager() throws Exception {
        keyManager = KeyManager.newKeyManager(datadir);
        File dir = new File(datadir+"/keystore");
        assertTrue(dir.exists() && dir.isDirectory() && dir.list().length==0);
    }

    @Test
    public void newAccount() throws Exception {
        newKeyManager();
        account = keyManager.newAccount(PASSWORD);
        String url = account.getURL();
        Address address = account.getAddress();
        String hex = address.getHex();
        assertTrue(hex.length() > 0 && hex.startsWith("0x"));
        File file = new File(url.substring(11));
        assertTrue(file.exists() && !file.isDirectory());
        //TODO test account locked
    }

    @Test
    public void newUnlockedAccount() throws Exception {
        newKeyManager();
        account = keyManager.newUnlockedAccount(PASSWORD);
        String url = account.getURL();
        Address address = account.getAddress();
        String hex = address.getHex();
        assertTrue(hex.length() > 0 && hex.startsWith("0x"));
        File file = new File(url.substring(11));
        assertTrue(file.exists() && !file.isDirectory());
        //TODO test account unlocked
    }

    @Test
    public void getAccounts() throws Exception {
        newKeyManager();
        newAccount();
        List<Account> accounts = keyManager.getAccounts();
        assertTrue(accounts.size()==1 && accounts.get(0).equals(account));
    }

    @Test
    public void accountExists() throws Exception {
        newKeyManager();
        newAccount();
        assertTrue(keyManager.accountExists(account));
    }

    @Test
    public void lockAccount() throws Exception {
        newKeyManager();
        newAccount();
        keyManager.lockAccount(account);
        //TODO test account locked
    }

    @Test
    public void unlockAccount() throws Exception {
        newKeyManager();
        newAccount();
        keyManager.unlockAccount(account,PASSWORD);
        //TODO test account unlocked
    }

    @Test
    public void unlockAccountDuring() throws Exception {
        newKeyManager();
        newAccount();
        keyManager.unlockAccountDuring(account,PASSWORD,1);
        //TODO test account locked after 1 sec
    }

    @Test
    public void deleteAccount() throws Exception {
        newKeyManager();
        newAccount();
        keyManager.deleteAccount(account,PASSWORD);
        File file = new File(account.getURL().substring(11));
        assertTrue(!file.exists());
    }

    @Test
    public void updateAccountPassphrase() throws Exception {
        newKeyManager();
        newAccount();
        String newPassword = PASSWORD+"2";
        keyManager.updateAccountPassphrase(account,PASSWORD,newPassword);
        try {
            keyManager.unlockAccount(account,PASSWORD);
            fail("passphrase not updated");
        }catch(Exception e ){}
        try {
            keyManager.unlockAccount(account, newPassword);
        }catch(Exception e){
            fail("passphrase updated but can't unlock with the new one");
        }
    }

    @Test
    public void signString() throws Exception {
        newKeyManager();
        newAccount();
        String hash =  Sha3.hash("tosign");
        keyManager.unlockAndsignString(account,PASSWORD, hash);
    }

}
