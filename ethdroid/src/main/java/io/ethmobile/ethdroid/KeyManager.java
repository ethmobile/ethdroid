package io.ethmobile.ethdroid;

import org.ethereum.geth.Account;
import org.ethereum.geth.Accounts;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.ethmobile.ethdroid.sha3.Sha3;

/**
 * Created by gunicolas on 16/05/17.
 */

public class KeyManager {

    private static final String KEYSTORE_DIRNAME = "/keystore";
    private KeyStore keystore;

    private KeyManager(String datadir) {
        File keystoreDir = new File(datadir + KEYSTORE_DIRNAME);
        if (!keystoreDir.exists()) keystoreDir.mkdir();
        keystore = Geth.newKeyStore(keystoreDir.getAbsolutePath(), Geth.LightScryptN,
            Geth.LightScryptP);
    }

    public static KeyManager newKeyManager(String datadir) {
        return new KeyManager(datadir);
    }

    public Account newAccount(String passphrase) throws Exception {
        return keystore.newAccount(passphrase);
    }

    public Account newUnlockedAccount(String passphrase) throws Exception {
        Account ret = newAccount(passphrase);
        unlockAccount(ret, passphrase);
        return ret;
    }

    public List<Account> getAccounts() throws Exception {
        List<Account> ret = new ArrayList<>();
        Accounts accounts = keystore.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            ret.add(accounts.get(i));
        }
        return ret;
    }

    public boolean accountExists(Account account) {
        if (account == null) return false;
        return keystore.hasAddress(account.getAddress());
    }

    public boolean accountIsLocked(Account account) {
        try {
            signString(account, "test");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // removes the private key with the given address from memory.
    public void lockAccount(Account account) throws Exception {
        keystore.lock(account.getAddress());
    }

    // unlockAccountDuring with 0 as duration
    public void unlockAccount(Account account, String passphrase) throws Exception {
        keystore.unlock(account, passphrase);
    }

    // seconds == 0 : until program exits
    public void unlockAccountDuring(Account account, String passphrase, long seconds)
        throws Exception {
        keystore.timedUnlock(account, passphrase, (long) (seconds * Math.pow(10, 9)));
    }

    public void deleteAccount(Account account, String passphrase) throws Exception {
        keystore.deleteAccount(account, passphrase);
    }

    public void updateAccountPassphrase(Account account, String passphrase, String newPassphrase)
        throws Exception {
        keystore.updateAccount(account, passphrase, newPassphrase);
    }

    public byte[] signString(Account account, String toSign) throws Exception {
        String hash = Sha3.hash(toSign);
        return keystore.signHash(account.getAddress(), Geth.newHashFromHex(hash).getBytes());

    }

    public byte[] unlockAndsignString(Account account, String password, String toSign)
        throws Exception {
        String hash = Sha3.hash(toSign);
        return keystore.signHashPassphrase(account, password, Geth.newHashFromHex(hash).getBytes());
    }

    public KeyStore getKeystore() {
        return keystore;
    }
}
