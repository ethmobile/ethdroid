package com.sqli.blockchain.ethdroid;

import org.ethereum.geth.Account;
import org.ethereum.geth.Accounts;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gunicolas on 16/05/17.
 */

public class KeyManager {

    private KeyStore keystore;

    private KeyManager(String datadir){
        keystore = Geth.newKeyStore(datadir,Geth.LightScryptN,Geth.LightScryptP);
    }

    public static KeyManager newKeyManager(String datadir){
        return new KeyManager(datadir);
    }

    public Account newAccount(String passphrase) throws Exception {
        return keystore.newAccount(passphrase);
    }

    public Account newUnlockedAccount(String passphrase) throws Exception{
        Account ret = newAccount(passphrase);
        unlockAccount(ret,passphrase);
        return ret;
    }

    public List<Account> getAccounts() throws Exception{
        List<Account> ret = new ArrayList<>();
        Accounts accounts = keystore.getAccounts();
        for(int i=0;i<accounts.size();i++){
            ret.add(accounts.get(i));
        }
        return ret;
    }

    public boolean accountExists(Account account){
        return keystore.hasAddress(account.getAddress());
    }

    public void lockAccount(Account account) throws Exception {
        keystore.lock(account.getAddress());
    }
    // unlockAccountDuring with 0 as duration
    public void unlockAccount(Account account,String passphrase) throws Exception{
        keystore.unlock(account,passphrase);
    }
    // durationNanoseconds == 0 : until program exits
    public void unlockAccountDuring(Account account,String passphrase,long durationNanoseconds) throws Exception {
        keystore.timedUnlock(account,passphrase,durationNanoseconds);
    }

    public void deleteAccount(Account account,String passphrase) throws Exception{
        keystore.deleteAccount(account,passphrase);
    }

    public void updateAccountPassphrase(Account account,String passphrase,String newPassphrase) throws Exception{
        keystore.updateAccount(account,passphrase,newPassphrase);
    }







}
