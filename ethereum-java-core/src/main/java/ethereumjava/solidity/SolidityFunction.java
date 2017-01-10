package ethereumjava.solidity;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.math.BigInteger;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;
import ethereumjava.module.objects.Block;
import ethereumjava.module.objects.BlockFilter;
import ethereumjava.module.objects.Hash;
import ethereumjava.module.objects.Transaction;
import ethereumjava.module.objects.TransactionRequest;
import ethereumjava.solidity.coder.SCoder;
import ethereumjava.solidity.coder.SCoderMapper;
import ethereumjava.solidity.coder.decoder.SDecoder;
import ethereumjava.solidity.types.SType;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by gunicolas on 4/08/16.
 */

public class SolidityFunction<T extends SType> extends SolidityElement {

    Object[] args;
    Class<T> returnType;

    public SolidityFunction(String address, Method method, Eth eth, Object[] args) {
        super(address, method, eth);
        this.args = args;
        this.returnType = method.getAnnotation(ReturnType.class).value();
    }

    @Override
    protected Class[] getParametersTypes() {
        return method.getParameterTypes();
    }

    @Override
    protected String signature() {
        return super.signature().substring(0, 8);
    }

    private String encode() {
        String encodedParameters = "";
        if (args != null) {
            encodedParameters = SCoder.encodeParams(args);
        }
        return "0x" + this.signature() + encodedParameters;
    }

    private T decodeResponse(String dataHex) {

        //TODO move to SCoder.decodeParam()

        Class<? extends SDecoder> decoderClass = SCoderMapper.getDecoderForClass(returnType);
        if (decoderClass == null) {
            throw new EthereumJavaException("no decoder found for type : " + returnType.getSimpleName());
        }

        try {
            return (T) decoderClass.newInstance().decode(dataHex); //TODO remove cast
        } catch (Exception e) {
            throw new EthereumJavaException(e);
        }
    }

    private TransactionRequest formatRequest(String from, BigInteger gas, BigInteger value) {
        //TODO can estimate gas before
        String payload = encode();
        TransactionRequest request = new TransactionRequest(from, address);
        request.setGas(gas);
        request.setDataHex(payload);
        if (value != null) {
            request.setValueHex(value);
        }
        return request;
    }

    private TransactionRequest formatRequest(String from, BigInteger gas) {
        return formatRequest(from, gas, null);
    }

    public Hash sendTransaction(String from, BigInteger gas) {
        return eth.sendTransaction(formatRequest(from, gas));
    }

    public Hash sendTransaction(String from, BigInteger gas, BigInteger value) {
        return eth.sendTransaction(formatRequest(from, gas, value));
    }

    public Observable<Transaction> sendTransactionAndGetMined(String from, BigInteger gas) {

        final Hash txHash = sendTransaction(from, gas);
        if (txHash == null) {
            return null;
        }

        return new BlockFilter(eth)
            .watch()
            .flatMap(getBlockFromHash())
            .flatMap(getTransactionsOfBlock())
            .first(keepGivenTransaction(txHash));
    }

    private Func1<Transaction, Boolean> keepGivenTransaction(final Hash txHash) {
        return new Func1<Transaction, Boolean>() {
            @Override
            public Boolean call(Transaction transaction) {
                return transaction.hash.getValue().equals(txHash.getValue());
            }
        };
    }

    private Func1<Block<Transaction>, Observable<Transaction>> getTransactionsOfBlock() {
        return new Func1<Block<Transaction>, Observable<Transaction>>() {
            @Override
            public Observable<Transaction> call(Block<Transaction> transactionBlock) {
                return Observable.from(transactionBlock.transactions);
            }
        };
    }

    private Func1<String, Observable<Block<Transaction>>> getBlockFromHash() {
        return new Func1<String, Observable<Block<Transaction>>>() {
            @Override
            public Observable<Block<Transaction>> call(String blockHash) {
                return eth.getBlock(Hash.valueOf(blockHash), Transaction.class);
            }
        };
    }

    public T call() {

        String payload = encode();
        TransactionRequest request = new TransactionRequest(address);
        request.setDataHex(payload);

        String encodedResponse = eth.call(request, "latest");

        return decodeResponse(encodedResponse);
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ReturnType {
        Class value();
    }

}
