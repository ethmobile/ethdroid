package io.ethmobile.ethdroid.model;

import static io.ethmobile.ethdroid.Utils.deleteDirIfExists;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.ethereum.geth.Account;

import java.io.File;

import io.ethmobile.ethdroid.ChainConfig;
import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by gunicolas on 19/05/17.
 */
//@RunWith(AndroidJUnit4.class)
public class TransactionTest {

    private static final String PASSWORD = "password";
    private Context appContext;
    private String datadir;
    private KeyManager keyManager;
    private Account account;
    private EthDroid eth;

    //@Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        datadir = appContext.getFilesDir().getAbsolutePath();

        //-- Setup keystore
        deleteDirIfExists(new File(datadir + "/keystore"));
        keyManager = KeyManager.newKeyManager(datadir);
        account = keyManager.newAccount(PASSWORD);

        //-- Setup node
        deleteDirIfExists(new File(datadir + "/GethDroid"));

        long networkID = 100;
        String genesis =
            "{\"config\": {\"chainId\": 100, \"homesteadBlock\": 0, \"eip155Block\": 0, "
                + "\"eip158Block\": 0 }, \"nonce\": \"0x0000000000000042\", \"timestamp\": "
                + "\"0x0\", \"parentHash\": "
                + "\"0x0000000000000000000000000000000000000000000000000000000000000000\", "
                + "\"extraData\": \"\", \"gasLimit\": \"0x8000000\", \"difficulty\": \"0x100\", "
                + "\"mixhash\": "
                + "\"0x0000000000000000000000000000000000000000000000000000000000000000\", "
                + "\"coinbase\": \"0x0000000000000000000000000000000000000042\", \"alloc\": {} }";
        String enode =
            "enode://a448517e9e7c6ae984c040791573b7e7b383461e34f546136e5e8ee8c3a4a61f8ee8f6836cb35a0b9e7de88bfaf5f01e528639a61357ab81f5fa8c5bc5e6a412@10.33.44.111:30301?discport:30302";

        eth = new EthDroid.Builder(datadir)
            .withChainConfig(new ChainConfig.Builder(networkID, genesis, enode).build())
            .withKeyManager(keyManager)
            .build();

        eth.start();
    }

    //@Test
    public void send() throws Exception {

        keyManager.unlockAccount(account, PASSWORD);

        //TODO invalid sender because of signer issue

        eth.newTransaction()
            .to("0xae58c341587161a2570212ce6f1fd88182c12e3b")
            .value(30)
            .send();
    }

}
