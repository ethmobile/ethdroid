package ethereumjava.module;

import ethereumjava.config.Config;
import ethereumjava.config.RPCTest;
import ethereumjava.exception.EthereumJavaException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by gunicolas on 22/11/16.
 */

//@FixMethodOrder(MethodSorters.NAME_ASCENDING) //to get listAccounts executed before newAccount
public class RPCPersonalModuleTest extends RPCTest {


    @Test
    public void listAccountsTest() throws Exception {
        List<String> accounts = ethereumJava.personal.listAccounts();
        assertTrue(accounts.size() == 2);
    }

    @Test
    public void unlockAccountSuccessTest() throws Exception {
        Config.TestAccount testAccount = config.accounts.get(0);
        boolean unlocked = ethereumJava.personal.unlockAccount(testAccount.id, testAccount.password, 3600);

        assertTrue(unlocked);
    }

    @Test
    public void unlockAccountFailTest() throws Exception {
        Config.TestAccount testAccount = config.accounts.get(0);
        try {
            ethereumJava.personal.unlockAccount(testAccount.id, testAccount.password + "00000", 3600);
        } catch (EthereumJavaException e) {
            List<String> accounts = ethereumJava.personal.listAccounts();
            assertTrue("could not decrypt key with given passphrase".compareTo(e.getMessage()) == 0);
        }
    }


    @Test
    public void newAccountTest() throws Exception {
        testCreateAccount();
        testUnlockAccount();
    }

    private void testCreateAccount() {
        String passwd = config.accounts.get(0).password;
        String account = ethereumJava.personal.newAccount(passwd);
        assertTrue(account.matches("^0x([0-9a-fA-F]){40}$"));
        List<String> accounts = ethereumJava.personal.listAccounts();
        assertTrue(accounts.get(accounts.size() - 1).compareTo(account) == 0); //get last
    }

    private void testUnlockAccount() {
        String passwd = config.accounts.get(0).password;
        String account = ethereumJava.personal.newAccount(passwd);
        assertTrue(account.matches("^0x([0-9a-fA-F]){40}$"));
        boolean unlocked = ethereumJava.personal.unlockAccount(account, passwd, 3600);
        assertTrue(unlocked);
    }

}
