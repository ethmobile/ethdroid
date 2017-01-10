package ethereumjava.module;

import ethereumjava.module.annotation.ConvertParam;
import ethereumjava.module.annotation.EthereumMethod;
import ethereumjava.module.annotation.ExcludeFromRequest;
import ethereumjava.module.annotation.GenericTypeIndex;
import ethereumjava.module.converter.GetBlockClassConverter;
import ethereumjava.module.objects.*;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;

/**
 * Eth module interfaced with the one exposed by Geth node.
 * Gives access to methods in order to communicate, write and read on the blockchain
 * See specifications at :
 * <p>
 * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3eth">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3eth</a>
 * </p>
 * <p>
 * It's an interface because calls are made using Java reflection.
 * <p>
 * Created by gunicolas on 23/08/16.
 */
public interface Eth {

    /**
     * Get the balance of the given account at a given block synchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetbalance">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetbalance</a>
     *
     * @param account   the account address to get the balance of
     * @param parameter the reference block used to get the balance
     * @return a BigInteger instance of the given account balance at the given block reference <b>in
     * WEI</b>.
     * @see ethereumjava.module.objects.Block.BlockParameter
     * @see BigInteger
     */
    @EthereumMethod(name = "getBalance")
    BigInteger balance(String account, Block.BlockParameter parameter);

    /**
     * Get the balance of the given account at a given block asynchronously. Returned Observable can
     * be subscribed to wait balance on a different thread. Prevents blocking the main thread.
     *
     * @param account   the account address to get the balance of
     * @param parameter the reference block used to get the balance
     * @return an Observable on a BigInteger instance of the given account balance at the given
     * block reference <b>in WEI</b>.
     * @see ethereumjava.module.objects.Block.BlockParameter
     * @see BigInteger
     * @see Observable
     */
    Observable<BigInteger> getBalance(String account, Block.BlockParameter parameter);


    /**
     * Get the block that corresponds to the given Hash synchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock</a>
     *
     * @param hash            the identifier of the wanted block
     * @param transactionType the class of the return type extending {@link TransactionFormat}
     * @param <T> the transaction format List of {@link Transaction} or {@link Hash} {@link TransactionFormat}
     * @return an instance of the block
     * @see TransactionFormat
     * @see Hash
     */
    @EthereumMethod(name = "getBlockByHash")
    @GenericTypeIndex(1)
    <T extends TransactionFormat> Block<T> block(Hash hash, @ConvertParam(with = GetBlockClassConverter.class) Class<T> transactionType);

    /**
     * Get the block that corresponds to the given Hash asynchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock</a>
     *
     * @param hash            the identifier of the wanted block
     * @param transactionType the class of the return type extending {@link TransactionFormat}
     * @param <T> the transaction format List of {@link Transaction} or {@link Hash} {@link TransactionFormat}
     * @return an instance of the block
     * @see TransactionFormat
     * @see Hash
     */
    @GenericTypeIndex(1)
    <T extends TransactionFormat> Observable<Block<T>> getBlock(Hash hash, @ConvertParam(with = GetBlockClassConverter.class) Class<T> transactionType);

    /**
     * Get the block that corresponds to the given number synchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock</a>
     *
     * @param number          the identifier of the wanted block
     * @param transactionType the class of the return type extending {@link TransactionFormat}
     * @param <T> the transaction format List of {@link Transaction} or {@link Hash} {@link TransactionFormat}
     * @return an instance of the block
     * @see TransactionFormat
     * @see BigInteger
     */
    @EthereumMethod(name = "getBlockByNumber")
    @GenericTypeIndex(1)
    <T extends TransactionFormat> Block<T> block(BigInteger number, @ConvertParam(with = GetBlockClassConverter.class) Class<T> transactionType);

    /**
     * Get the block that corresponds to the given Hash asynchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgetblock</a>
     *
     * @param number          the identifier of the wanted block
     * @param transactionType the class of the return type extending {@link TransactionFormat}
     * @param <T> the transaction format List of {@link Transaction} or {@link Hash} {@link TransactionFormat}
     * @return an instance of the block
     * @see TransactionFormat
     * @see BigInteger
     */
    @GenericTypeIndex(1)
    <T extends TransactionFormat> Observable<Block<T>> getBlock(BigInteger number, @ConvertParam(with = GetBlockClassConverter.class) Class<T> transactionType);

    /**
     * Get the transaction that corresponds to the given hash synchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransaction">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransaction</a>
     *
     * @param hash the identifier of the wanted transaction
     * @return an instance of the transaction
     * @see Transaction
     * @see Hash
     */
    @EthereumMethod(name = "getTransactionByHash")
    Transaction transaction(Hash hash);

    /**
     * Get the transaction that corresponds to the given hash asynchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransaction">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransaction</a>
     *
     * @param hash the identifier of the wanted transaction
     * @return an instance of the transaction
     * @see Transaction
     * @see Hash
     */
    Observable<Transaction> getTransaction(Hash hash);

    /**
     * Get the transaction receipt that corresponds to the given transaction hash synchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransactionreceipt">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransactionreceipt</a>
     *
     * @param hash the identifier of the transaction associated to the wanted receipt
     * @return an instance of the receipt
     * @see TransactionReceipt
     * @see Hash
     */
    @EthereumMethod(name = "getTransactionReceipt")
    TransactionReceipt transactionReceipt(Hash hash);

    /**
     * Get the transaction receipt that corresponds to the given transaction hash asynchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransactionreceipt">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethgettransactionreceipt</a>
     *
     * @param hash the identifier of the transaction associated to the wanted receipt
     * @return an instance of the receipt
     * @see TransactionReceipt
     * @see Hash
     */
    Observable<TransactionReceipt> getTransactionReceipt(Hash hash);

    /**
     * Send a transaction synchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethsendtransaction">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethsendtransaction</a>
     *
     * @param request the request of the transaction
     * @return the hash of the transaction
     * @see Transaction
     * @see Hash
     */
    Hash sendTransaction(TransactionRequest request);

    /**
     * Send a transaction asynchronously
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethsendtransaction">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethsendtransaction</a>
     *
     * @param request the request of the transaction
     * @return the hash of the transaction
     * @see Transaction
     * @see Hash
     */
    @EthereumMethod(name = "sendTransaction")
    Observable<Hash> sendTransactionAsync(TransactionRequest request);

    /**
     * Executes a message call transaction, which is directly executed in the VM of the node, but never mined into the blockchain.
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethcall">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethcall</a>
     *
     * @param request     the request of the transaction
     * @param callOnBlock the block used for the transaction (Can be one of "earliest", "pending" or "latest")
     * @return the data of the call
     * @see TransactionRequest
     */
    String call(TransactionRequest request, String callOnBlock);

    /**
     * Create a filter to notify a change on the network
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethfilter">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethfilter</a>
     *
     * @param options the options of the wanted filter
     * @return the id of the filter
     * @see FilterOptions
     */
    Observable<String> newFilter(FilterOptions options);

    /**
     * Create a filter to notify when a new block arrives
     * <p>
     * <a href="https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethfilter">https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethfilter</a>
     *
     * @return the id of the filter
     * @see FilterOptions
     */
    Observable<String> newBlockFilter();

    /**
     * Return the logs relatives to the filterID
     *
     * @param filterId filter id got with eth.newFilter()
     * @return a list of logs
     * @see Log
     */
    @EthereumMethod(name = "getFilterLogs")
    Observable<Log[]> getFilterLogs(String filterId);

    /**
     * Polling method for a filter, which returns an array of logs which occurred since last poll.
     *
     * @param filterId filter id got with eth.newFilter()
     * @param logType  DefaultFilter (Log)
     * @param <T> Filter returned object type
     * @return an observable of List of T objects. Logs of changes since last poll
     */
    @EthereumMethod(name = "getFilterChanges")
    @GenericTypeIndex(1)
    <T> Observable<List<T>> getFilterChanges(String filterId, @ExcludeFromRequest Class<T> logType);


    /**
     * Disable and delete the filter.
     *
     * @param filterId filter id got with eth.newFilter()
     * @return an observable
     */
    Observable uninstallFilter(String filterId);


}
