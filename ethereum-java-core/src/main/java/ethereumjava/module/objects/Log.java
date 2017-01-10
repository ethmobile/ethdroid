package ethereumjava.module.objects;

import java.math.BigInteger;

/**
 * Created by gunicolas on 14/10/16.
 */
public class Log {

    public boolean removed;
    public BigInteger logIndex;
    public BigInteger transactionIndex;
    public String transactionHash;
    public String blockHash;
    public BigInteger blockNumber;
    public String address;
    public String data;
    public String[] topics;

    public Log(boolean removed, BigInteger logIndex, BigInteger transactionIndex, String transactionHash, String blockHash, BigInteger blockNumber, String address, String data, String[] topics) {
        this.removed = removed;
        this.logIndex = logIndex;
        this.transactionIndex = transactionIndex;
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.address = address;
        this.data = data;
        this.topics = topics;
    }
}
