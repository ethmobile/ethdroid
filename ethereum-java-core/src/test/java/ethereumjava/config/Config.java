package ethereumjava.config;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by gunicolas on 22/11/16.
 */
public final class Config {

    private static final String PATH = "src/test/resources/config.json";
    private static Config INSTANCE;
    public int rpcProviderPort;
    public String rpcProviderAddr;
    public String ethAddr;
    public int ethPort;
    public int networkId;
    public String difficulty;
    public String contractAddress;
    public List<TestAccount> accounts;

    private Config(int rpcProviderPort, String rpcProviderAddr, String ethAddr, int ethPort, int networkId, String difficulty, String contractAddress, List<TestAccount> accounts) {
        this.rpcProviderPort = rpcProviderPort;
        this.rpcProviderAddr = rpcProviderAddr;
        this.ethAddr = ethAddr;
        this.ethPort = ethPort;
        this.networkId = networkId;
        this.difficulty = difficulty;
        this.contractAddress = contractAddress;
        this.accounts = accounts;
    }

    public static Config newInstance() throws FileNotFoundException {
        FileReader reader = new FileReader(PATH);
        Gson deserializer = new Gson();
        INSTANCE = deserializer.fromJson(reader, Config.class);
        return INSTANCE;
    }

    public class TestAccount {
        public String id;
        public String password;
    }
}
