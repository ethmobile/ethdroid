package com.sqli.blockchain.ethdroid.model;

import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.exception.EthDroidException;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Context;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Hash;
import org.ethereum.geth.KeyStore;

/**
 * Created by gunicolas on 19/05/17.
 */


public class Transaction {

    private static final String DEFAULT_RECIPIENT_ADDRESS = "";
    private static final long DEFAULT_VALUE = 0;
    private static final long DEFAULT_GAS_AMOUNT = 90000;
    private static final long DEFAULT_GAS_PRICE = 0;

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
    private org.ethereum.geth.Transaction raw;

    /**
     * Initialize transaction with default values :
     * - given recipient (can't be null)
     * - default or set main account as sender (based on context)
     * - nonce of the sender or 0 if no sender found in context
     * - default value
     * - default gas amount
     * - gas price suggested by geth via JNI
     * - default data
     * @param eth base context of the transaction
     * @throws Exception //TODO
     */
    public Transaction(EthDroid eth) throws Exception {
        if( eth == null ) throw new EthDroidException("context reference can't be null");
        this.eth = eth;
        this.txContext = eth.getMainContext();
        this.from = eth.getMainAccount();
        this.to = this.from.getAddress();
        if( this.from != null ) this.nonce = eth.getClient().getPendingNonceAt(txContext,from.getAddress());
        this.value = Geth.newBigInt(DEFAULT_VALUE);
        this.gas = Geth.newBigInt(DEFAULT_GAS_AMOUNT);
        this.gasPrice = eth.getClient().suggestGasPrice(txContext);
        this.data = new byte[]{};
    }

    public Transaction nonce(long nonce){
        this.nonce = nonce;
        return this;
    }
    public Transaction to(Address account){
        if( to == null ) throw new EthDroidException("recipient can't be null");
        this.to = account;
        return this;
    }
    public Transaction to(String address) throws Exception {
        return to(Geth.newAddressFromHex(address));
    }
    public Transaction from(Account account, String passphrase){
        if( account == null ) throw new EthDroidException("sender can't be null");
        this.from = account;
        this.fromPassphrase = passphrase;
        return this;
    }
    public Transaction from(Account account){
        //TODO test if account is unlocked
        if( account == null ) throw new EthDroidException("sender can't be null");
        this.from = account;
        return this;
    }
    public Transaction value(BigInt value){
        this.value = value;
        return this;
    }
    public Transaction value(long value){
        return value(Geth.newBigInt(value));
    }
    public Transaction gasAmount(BigInt gas){
        this.gas = gas;
        return this;
    }
    public Transaction gasPrice(BigInt gasPrice){
        this.gasPrice = gasPrice;
        return this;
    }
    public Transaction data(byte[] data){
        this.data = data;
        return this;
    }
    public Transaction data(String data){
        this.data = data.getBytes();
        return this;
    }
    public Transaction context(Context context){
        this.txContext = context;
        return this;
    }


    /**
     * Get raw geth transaction.
     * Returned transaction is not signed, so it can't be sent.
     * @return not signed transaction
     */
    public org.ethereum.geth.Transaction getRawTransaction() throws Exception {
        this.raw = Geth.newTransaction(nonce,to,value,gas,gasPrice,data);
        return this.raw;
    }

    public Hash send() throws Exception{
        if( to == null ) throw new EthDroidException("recipient can't be null");
        if( from == null ) throw new EthDroidException("sender can't be null");
        if( this.eth.getKeyManager() == null ) throw new EthDroidException("no key manager defined");
        org.ethereum.geth.Transaction raw = getRawTransaction();
        KeyStore keystore = this.eth.getKeyManager().getKeystore();
        BigInt networkId = Geth.newBigInt(eth.getChainConfig().getNetworkID());
        if( this.fromPassphrase == null ) this.raw = keystore.signTx(from,raw,networkId);
        else this.raw = keystore.signTxPassphrase(from,fromPassphrase,raw,networkId);
        this.eth.getClient().sendTransaction(this.eth.getMainContext(),this.raw);
        return this.raw.getHash();
    }
}

