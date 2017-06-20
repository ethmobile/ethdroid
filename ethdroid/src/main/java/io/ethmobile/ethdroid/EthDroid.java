package io.ethmobile.ethdroid;

import org.ethereum.geth.Account;
import org.ethereum.geth.Context;
import org.ethereum.geth.EthereumClient;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Header;
import org.ethereum.geth.Node;
import org.ethereum.geth.SyncProgress;

import java.util.List;

import io.ethmobile.ethdroid.exception.EthDroidException;
import io.ethmobile.ethdroid.model.Balance;
import io.ethmobile.ethdroid.model.Filter;
import io.ethmobile.ethdroid.model.Transaction;
import io.ethmobile.ethdroid.solidity.Contract;
import io.ethmobile.ethdroid.solidity.ContractType;
import io.reactivex.Observable;

/**
 * Created by gunicolas on 16/05/17.
 */

public class EthDroid {

    private String datadir;
    private Context mainContext;
    private Node node;
    private ChainConfig chainConfig;
    private EthereumClient client;
    private KeyManager keyManager;
    private Account mainAccount;

    private EthDroid() {
    }

    private static void setMainAccountAtIndex(EthDroid context, int keyManagerIndex)
        throws Exception {
        if (context.keyManager == null) throw new EthDroidException("no key manager configured");
        context.mainAccount = context.keyManager.getAccounts().get(keyManagerIndex);
    }

    /*
    * Set the main account by default if none has been defined.
    * By default the main account is the first one in the key manager.
    * If no key manager is referenced, main account is null.
    * If there is no accounts in key manager, main account is null.
    */
    private static void setDefaultMainAccount(EthDroid eth) throws Exception {
        if (eth.mainAccount == null && eth.keyManager != null) {
            List<Account> accounts = eth.keyManager.getAccounts();
            if (accounts.size() > 0) {
                eth.mainAccount = accounts.get(0);
            }
        }
    }

    private static void setMainAccount(EthDroid eth, Account mainAccount) {
        if (eth.keyManager != null && !eth.keyManager.accountExists(mainAccount)) {
            throw new EthDroidException("given account doesn't exist in key manager");
        }
        eth.mainAccount = mainAccount;
    }

    public void start() throws Exception {
        node.start();
        client = node.getEthereumClient();
    }

    public void stop() throws Exception {
        node.stop();
    }

    public boolean isSyncing() throws Exception {
        return client.syncProgress(mainContext) != null;
    }

    public boolean isSynced() throws Exception {
        SyncProgress progress = client.syncProgress(mainContext);
        return progress.getCurrentBlock() >= progress.getHighestBlock();
    }

    public <T extends ContractType> T getContractInstance(Class<T> contractAbi, String address) {
        return Contract.getContractInstance(this, contractAbi, address);
    }

    public Transaction newTransaction() throws Exception {
        return new Transaction(this);
    }

    public Observable<Header> newHeadFilter() {
        return Filter.newHeadFilter(this);
    }

    public Balance getBalance() throws Exception {
        return getBalanceOf(mainAccount);
    }

    public Balance getBalanceOf(Account account) throws Exception {
        return Balance.of(client.getPendingBalanceAt(mainContext, account.getAddress()).getInt64());
    }

    public ChainConfig getChainConfig() {
        return chainConfig;
    }

    public Context getMainContext() {
        return mainContext;
    }

    public EthereumClient getClient() {
        return client;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    /**
     * Set a key manager to the context
     * If the current main account is not in the given key manager, it became the default account of
     * key manager.
     *
     * @param keyManager the new key manager
     * @throws Exception Exceptions can be thrown by Geth through JNI
     */
    public void setKeyManager(KeyManager keyManager) throws Exception {
        this.keyManager = keyManager;
        if (!this.keyManager.accountExists(this.mainAccount)) this.mainAccount = null;
        if (this.mainAccount == null) setDefaultMainAccount(this);
    }

    public Account getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(Account mainAccount) {
        setMainAccount(this, mainAccount);
    }

    public void setMainAccountAtIndex(int keyManagerIndex) throws Exception {
        setMainAccountAtIndex(this, keyManagerIndex);
    }

    public static class Builder {

        EthDroid build;

        /**
         * Parametrized Builder with the default values :
         * <ul>
         * <li>Context : @{@link Builder#withCancelContext()}</li>
         * </ul>
         *
         * @param datadir Directory where to store Geth files
         */
        public Builder(String datadir) {
            build = new EthDroid();
            withDefaultContext();
            withDatadirPath(datadir);
        }

        /**
         * Release resources when execution is over.
         *
         * @return reference on the parametrized builder
         */
        public Builder withDefaultContext() {
            build.mainContext = Geth.newContext();
            return this;
        }

        /**
         * Release resources when execution is over or when a cancel is asked
         *
         * @return reference on the parametrized builder
         */
        public Builder withCancelContext() {
            build.mainContext = Geth.newContext().withCancel();
            return this;
        }

        /**
         * Release resources when execution is over or when it reach the deadline (golang context).
         *
         * @param seconds     //TODO
         * @param nanoseconds //TODO
         * @return reference on the parametrized builder
         */
        public Builder withDeadlineContext(long seconds, long nanoseconds) {
            build.mainContext = Geth.newContext().withDeadline(seconds, nanoseconds);
            return this;
        }

        /**
         * Release resources when execution is over or when @seconds passed since the function call.
         *
         * @param seconds number of seconds to wait before canceling the call
         * @return reference on the parametrized builder
         */
        public Builder withTimeoutContext(long seconds) {
            build.mainContext = Geth.newContext().withTimeout(seconds);
            return this;
        }

        /**
         * Set path where node files will be saved (node key, blockchain db, ...)
         *
         * @param datadir string path where to save node files
         * @return reference on the parametrized builder
         */
        public Builder withDatadirPath(String datadir) {
            build.datadir = datadir;
            return this;
        }

        public Builder onPublicNetwork(ChainConfig.NETWORK network) {
            switch (network) {
                case HOMESTEAD:
                    return onMainnet();
                case ROPSTEN:
                    return onTestnet();
                case RINKEBY:
                    return onRinkeby();
                default:
                    throw new IllegalArgumentException("Unknwon network id : " + network);
            }
        }

        public Builder onMainnet() {
            build.chainConfig = ChainConfig.getMainnetConfig();
            return this;
        }

        public Builder onTestnet() {
            build.chainConfig = ChainConfig.getTestnetConfig();
            return this;
        }

        public Builder onRinkeby() {
            build.chainConfig = ChainConfig.getRinkebyConfig();
            return this;
        }

        public Builder withChainConfig(ChainConfig chainConfig) {
            build.chainConfig = chainConfig;
            return this;
        }

        public Builder withKeyManager(KeyManager keyManager) {
            build.keyManager = keyManager;
            return this;
        }

        public Builder withMainAccount(Account mainAccount) {
            setMainAccount(build, mainAccount);
            return this;
        }

        public Builder withMainAccountAtIndex(int keyManagerIndex) throws Exception {
            setMainAccountAtIndex(build, keyManagerIndex);
            return this;
        }

        public EthDroid build() throws Exception {
            setDefaultMainAccount(build);
            build.node = Geth.newNode(build.datadir, build.chainConfig.nodeConfig);
            return build;
        }
    }
}
