package io.ethmobile.ethdroid.model;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Block;
import org.ethereum.geth.CallMsg;
import org.ethereum.geth.Context;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Hash;
import org.ethereum.geth.KeyStore;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.Utils;
import io.ethmobile.ethdroid.exception.EthDroidException;
import io.ethmobile.ethdroid.exception.SmartContractException;
import io.reactivex.Observable;
import okio.ByteString;

/**
 * Created by gunicolas on 19/05/17.
 */


public class Transaction {

    private static final String DEFAULT_RECIPIENT_ADDRESS = "";
    private static final long DEFAULT_VALUE = 0;
    private static final long DEFAULT_GAS_AMOUNT = 90000;
    private static final long DEFAULT_GAS_PRICE = 0;

    private static final String NO_RECIPIENT_ERROR = "recipient can't be null";
    private static final String NO_SENDER_ERROR = "sender can't be null";
    private static final String NO_KEYMANAGER_ERROR = "no key manager defined";
    private static final String NO_CONTEXT_ERROR = "context reference can't be null";

    private long nonce;
    private Address to;
    private Account from;
    private String fromPassphrase; // if null then related account supposed to be unlocked
    private BigInt value;
    private BigInt gas;
    private BigInt gasPrice;
    private byte[] data;

    private Context txContext;
    private EthDroid eth;

    /**
     * Initialize transaction with default values :
     * - given recipient (can't be null)
     * - default or set main account as sender (based on context)
     * - nonce of the sender or 0 if no sender found in context
     * - default value
     * - default gas amount
     * - gas price suggested by geth via JNI
     * - default data
     *
     * @param eth base context of the transaction
     * @throws Exception //TODO
     */
    public Transaction(EthDroid eth) throws Exception {
        if (eth == null) throw new EthDroidException(NO_CONTEXT_ERROR);
        this.eth = eth;
        this.txContext = eth.getMainContext();
        this.from = eth.getMainAccount();
        this.to = this.from.getAddress();
        if (this.from != null) {
            this.nonce = eth.getClient().getPendingNonceAt(txContext,
                from.getAddress());
        }
        this.value = Geth.newBigInt(DEFAULT_VALUE);
        this.gas = Geth.newBigInt(DEFAULT_GAS_AMOUNT);
        this.gasPrice = eth.getClient().suggestGasPrice(txContext);
        this.data = new byte[]{};
    }

    public Transaction nonce(long nonce) {
        this.nonce = nonce;
        return this;
    }

    public Transaction to(Address account) {
        if (to == null) throw new EthDroidException(NO_RECIPIENT_ERROR);
        this.to = account;
        return this;
    }

    public Transaction to(String address) throws Exception {
        return to(Geth.newAddressFromHex(address));
    }

    public Transaction from(Account account, String passphrase) {
        if (account == null) throw new EthDroidException(NO_SENDER_ERROR);
        this.from = account;
        this.fromPassphrase = passphrase;
        return this;
    }

    public Transaction from(Account account) {
        //TODO test if account is unlocked
        if (account == null) throw new EthDroidException(NO_SENDER_ERROR);
        this.from = account;
        return this;
    }

    public Transaction value(BigInt value) {
        this.value = value;
        return this;
    }

    public Transaction value(long value) {
        return value(Geth.newBigInt(value));
    }

    public Transaction gasAmount(BigInt gas) {
        this.gas = gas;
        return this;
    }

    public Transaction gasPrice(BigInt gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public Transaction data(byte[] data) {
        this.data = data;
        return this;
    }

    /**
     * Set transaction data from given hexadecimal byte array in string format
     * data can be prefixed by '0x' hexadecimal identifier
     *
     * @param data String date used to build the transaction
     * @return Transaction built from data
     */
    public Transaction data(String data) {
        if (data.contains("0x")) data = data.substring(2);
        data(ByteString.decodeHex(data).toByteArray());
        return this;
    }

    /**
     * Set context in the transaction
     *
     * @param context Context to set in the transaction
     * @return Updated current transaction
     */
    public Transaction context(Context context) {
        this.txContext = context;
        return this;
    }

    private boolean checkValidity() throws Exception {
        if (to == null) throw new EthDroidException(NO_RECIPIENT_ERROR);
        if (from == null) throw new EthDroidException(NO_SENDER_ERROR);
        return true;
    }

    /**
     * Get raw geth transaction.
     * Returned transaction is not signed, so it can't be sent.
     *
     * @return not signed transaction
     * @throws Exception An error occured
     */
    public org.ethereum.geth.Transaction getRawTransaction() throws Exception {
        if (to == null) throw new EthDroidException(NO_RECIPIENT_ERROR);
        return Geth.newTransaction(nonce, to, value, gas, gasPrice, data);
    }

    private org.ethereum.geth.Transaction sign() throws Exception {
        org.ethereum.geth.Transaction raw = getRawTransaction();
        if (from == null) throw new EthDroidException(NO_SENDER_ERROR);
        if (this.eth.getKeyManager() == null) throw new EthDroidException(NO_KEYMANAGER_ERROR);
        KeyStore keystore = this.eth.getKeyManager().getKeystore();
        BigInt networkId = Geth.newBigInt(eth.getChainConfig().getNetworkID());
        if (this.fromPassphrase == null) {
            raw = keystore.signTx(from, raw, networkId);
        } else {
            raw = keystore.signTxPassphrase(from, fromPassphrase, raw, networkId);
        }
        return raw;
    }

    private CallMsg toCallMessage() throws Exception {
        checkValidity();
        CallMsg ret = Geth.newCallMsg();
        ret.setFrom(from.getAddress());
        ret.setTo(to);
        ret.setData(data);
        ret.setValue(value);
        ret.setGas(gas.getInt64());
        ret.setGasPrice(gasPrice);
        return ret;
    }

    public String call() throws Exception {
        byte[] hexadecimalResult = this.eth.getClient().pendingCallContract(txContext,
            toCallMessage());
        if (hexadecimalResult == null) {
            throw new SmartContractException(
                "Smart-Contract function (" + data + ") throws Exception or doesn't exist");
        }
        return ByteString.of(hexadecimalResult).hex();
    }

    public Hash send() throws Exception {
        org.ethereum.geth.Transaction raw = sign();
        this.eth.getClient().sendTransaction(this.eth.getMainContext(), raw);
        return raw.getHash();
    }

    public Observable<Block> sendWithNotification() throws Exception {
        Hash txHash = send();

        return Filter.newHeadFilter(eth)
            .flatMap(header -> {
                try {
                    return Observable.just(
                        eth.getClient().getBlockByHash(eth.getMainContext(), header.getHash()));
                } catch (Exception e) {
                    return Observable.error(e.getCause());
                }
            })
            .filter(block -> {
                try {
                    return Utils.transactionListContains(block.getTransactions(), txHash);
                } catch (Exception e) {
                    return false;
                }
            });
    }
}

