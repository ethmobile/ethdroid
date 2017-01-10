package ethereumjava.module;

import java.util.List;

/**
 * Personal module like the one exposed by Geth node.
 * Gives access to methods to manage, control and monitor accounts on the node.
 * See specifications at :
 *
 * It's an interface because calls are made using Java reflection.
 *
 * Created by gunicolas on 17/08/16.
 * @see <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personal">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personal</a>
 */
public interface Personal {

    /**
     * List all accounts/wallets available on the node.
     *
     * @return a list of account address, available on the node
     * @see <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personallistaccounts">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personallistaccounts</a>
     */
    List<String> listAccounts();

    /**
     * Unlock the account with the given address, password and during the given duration.
     *
     * @param address  account address, given when account is created.
     * @param password account password, set at the account creation
     * @param duration duration of the unlocked session
     * @return true if the account has been unlocked with the given password
     * @see <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personalunlockaccount">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personalunlockaccount</a>
     */
    boolean unlockAccount(String address, String password, int duration);

    /**
     * Creates a new account/wallet on the node, protected by the given password
     *
     * @param password password which will be used to unlock the account
     * @return generated address of the newly created account
     * @see <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personalnewaccount">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#personalnewaccount</a>
     */
    String newAccount(String password);

}
