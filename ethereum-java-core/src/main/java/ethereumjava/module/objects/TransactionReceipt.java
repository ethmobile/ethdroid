package ethereumjava.module.objects;

import java.math.BigInteger;

/**
 * Created by gunicolas on 23/08/16.
 */
public class TransactionReceipt {

    public Hash blockHash;
    public BigInteger blockNumber;
    public Hash transactionHash;
    public BigInteger transactionIndex;
    public String from;
    public String to;


    public BigInteger cumulativeGasUsed;
    public BigInteger gasUsed;
    public String contractAddress;


    @Override
    public String toString() {
        return "TransactionReceipt{" +
            "blockHash='" + blockHash + '\'' +
            ", blockNumber=" + blockNumber +
            ", transactionHash='" + transactionHash + '\'' +
            ", transactionIndex=" + transactionIndex +
            ", from='" + from + '\'' +
            ", to='" + to + '\'' +
            ", cumulativeGasUsed=" + cumulativeGasUsed +
            ", gasUsed=" + gasUsed +
            ", contractAddress='" + contractAddress + '\'' +
            '}';
    }
}
