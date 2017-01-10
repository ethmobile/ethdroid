package ethereumjava.module.objects;

import java.math.BigInteger;
import java.util.List;


/**
 * Created by gunicolas on 23/08/16.
 */
public class Block<T extends TransactionFormat> {

    public BigInteger number;
    public Hash hash;
    public Hash parentHash;
    public String nonce;
    public String sha3Uncles;
    public String logsBloom;
    public String transactionsRoot;
    public String stateRoot;
    public String miner;
    public BigInteger difficulty;
    public BigInteger totalDifficulty;
    public String extraData;
    public BigInteger size;
    public BigInteger gasLimit;
    public BigInteger gasUsed;
    public BigInteger timestamp;
    public List<T> transactions;
    public List<String> uncles;

    @Override
    public String toString() {
        return "Block{" +
            "number=" + number +
            ", hash='" + hash + '\'' +
            ", parentHash='" + parentHash + '\'' +
            ", nonce='" + nonce + '\'' +
            ", sha3Uncles='" + sha3Uncles + '\'' +
            ", logsBloom='" + logsBloom + '\'' +
            ", transactionsRoot='" + transactionsRoot + '\'' +
            ", stateRoot='" + stateRoot + '\'' +
            ", miner='" + miner + '\'' +
            ", difficulty=" + difficulty +
            ", totalDifficulty=" + totalDifficulty +
            ", extraData='" + extraData + '\'' +
            ", size=" + size +
            ", gasLimit=" + gasLimit +
            ", gasUsed=" + gasUsed +
            ", timestamp=" + timestamp +
            ", transactions=" + transactions +
            ", uncles=" + uncles +
            '}';
    }

    public enum BlockParameter {
        LATEST("latest"),
        PENDING("pending"),
        EARLIEST("earliest");

        private String name;

        BlockParameter(String name) {
            this.name = name;
        }

        public String toString() {
            return "\"" + name + "\"";
        }
    }
}
