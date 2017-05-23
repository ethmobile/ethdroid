package com.sqli.blockchain.ethdroid_sample;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sqli.blockchain.ethdroid.ChainConfig;
import com.sqli.blockchain.ethdroid.EthDroid;
import com.sqli.blockchain.ethdroid.KeyManager;
import com.sqli.blockchain.ethdroid.solidity.types.SUInt;

import org.ethereum.geth.Account;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button sendMoneyButton;
    Button sendTxSCButton;
    Button callTxButton;

    private EthDroid ethdroid;
    private KeyManager keyManager;
    private Account account;
    private static final String PASSWORD = "password";
    private ITestContract contract;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

        sendMoneyButton = (Button) findViewById(R.id.sendmoney_button);
        sendTxSCButton = (Button) findViewById(R.id.sendTx_SC_button);
        callTxButton = (Button) findViewById(R.id.callSC_button);
        sendMoneyButton.setOnClickListener(this);
        sendTxSCButton.setOnClickListener(this);
        callTxButton.setOnClickListener(this);


        String datadir = getFilesDir().getAbsolutePath();

        //deleteDirIfExists(new File(datadir+"/GethDroid"));
        //deleteDirIfExists(new File(datadir+"/keystore"));

        long networkID = 100;
        String genesis = "{\"config\": {\"chainId\": 100, \"homesteadBlock\": 0, \"eip155Block\": 0, \"eip158Block\": 0 }, \"nonce\": \"0x0000000000000042\", \"timestamp\": \"0x0\", \"parentHash\": \"0x0000000000000000000000000000000000000000000000000000000000000000\", \"extraData\": \"\", \"gasLimit\": \"0x8000000\", \"difficulty\": \"0x100\", \"mixhash\": \"0x0000000000000000000000000000000000000000000000000000000000000000\", \"coinbase\": \"0x0000000000000000000000000000000000000042\", \"alloc\": {} }";
        String enode = "enode://a448517e9e7c6ae984c040791573b7e7b383461e34f546136e5e8ee8c3a4a61f8ee8f6836cb35a0b9e7de88bfaf5f01e528639a61357ab81f5fa8c5bc5e6a412@10.33.44.111:30301?discport:30302";

        try {
            keyManager = KeyManager.newKeyManager(datadir);
            //account = keyManager.newUnlockedAccount(PASSWORD);
            account = keyManager.getAccounts().get(0);
            keyManager.unlockAccount(account,PASSWORD);

            Log.print(account.getAddress().getHex());

            ethdroid = new EthDroid.Builder(datadir)
                .withChainConfig(new ChainConfig.Builder(networkID, genesis, enode).build())
                .withKeyManager(keyManager)
                .build();

            ethdroid.start();

            contract = ethdroid.getContractInstance(ITestContract.class,"0x06ad6dfb208e82a11dc244815498461ba21727af");

            ethdroid.newHeadFilter().subscribe(header -> Log.print(header.getHash().getHex()));

            contract.simpleEvent().listen()
                .subscribe(event -> Log.print(event.getElement1().asString()));

        }catch(Exception e){
            Log.print(e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if( v == sendMoneyButton ){
                ethdroid.newTransaction()
                    .to(account.getAddress())
                    .value(30)
                    .send();
            } else if( v == sendTxSCButton ){
                contract.bar(SUInt.SUInt8.fromShort((short) 3)).send();
            } else if( v == callTxButton ){
                Log.print(contract.value().call().getElement1().asString());
            }
        } catch (Exception e) {
            Log.print(e.getMessage());
        }

    }

    @Override
    protected void onStop() {
        try {
            ethdroid.stop();
        } catch (Exception e) {
            Log.print(e.getMessage());
        }
        super.onStop();
    }

}
