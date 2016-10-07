import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sqli.blockchain.android_geth.EthereumActivity;
import com.sqli.blockchain.ethereum_android_sample.R;

import java.math.BigDecimal;
import java.math.BigInteger;

import ethereumjava.module.objects.Block;
import ethereumjava.module.objects.Hash;
import ethereumjava.module.objects.Transaction;
import ethereumjava.module.objects.TransactionReceipt;
import ethereumjava.module.objects.TransactionRequest;
import ethereumjava.solidity.ContractType;
import ethereumjava.solidity.SolidityFunction;
import ethereumjava.solidity.SolidityUtils;


public class MainActivity extends EthereumActivity implements View.OnClickListener{

    static final String TAG = MainActivity.class.getSimpleName();

    Button button;
    Button tx;
    Button contract;

    String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        tx = (Button) findViewById(R.id.tx);
        tx.setOnClickListener(this);

        contract = (Button) findViewById(R.id.contract);
        contract.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        enableButtons(false);
        super.onResume();
    }

    @Override
    public void onEthereumServiceReady() {
        super.onEthereumServiceReady();
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                enableButtons(true);
            }
        });
    }


    private void getBlock(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Block<Hash> b = web3J.eth.block(BigInteger.valueOf(0),Hash.class);
                }catch(Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }
    private void getTransaction(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transaction transaction = web3J.eth.transaction(Hash.valueOf("0x253c0d8019558dae73af30fe2281e9177df696c26545b520770a64cd475b48f4"));
                }catch(Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }
    private void getTransactionReceipt(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TransactionReceipt transactionReceipt = web3J.eth.transactionReceipt(Hash.valueOf("0x253c0d8019558dae73af30fe2281e9177df696c26545b520770a64cd475b48f4"));
                }catch(Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }

    private void createAccount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    account = web3J.personal.newAccount("toto");
                    Log.i(TAG,"new account : :"+account);


                }catch(Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }

    private void sendTransaction(){
        try {
            boolean unlocked = web3J.personal.unlockAccount(account,"toto",3600);
            if( unlocked ) {
                BigDecimal amount = SolidityUtils.toWei("2", "ether");
                String amountHex = SolidityUtils.toHex(amount);
                TransactionRequest tx = new TransactionRequest(account,"0xf1e04ff9007ee1e0864cd39270a407c71b14b7e2",amountHex,"4 u");

                Hash txHash = web3J.eth.sendTransaction(tx);

            }
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    private void enableButtons(final boolean enable){
        button.setEnabled(enable);
    }

    interface Contract extends ContractType {
        SolidityFunction<Void> RentMe();
    }

    private void contract(){

        final String CONTRACT_ADDRESS = "0xdc89cbd768000fcf75b324cb97f5dfbd8b943ef6";

        Contract contract = (Contract) web3J.contract.withAbi(Contract.class).at(CONTRACT_ADDRESS); //TODO remove cast

        System.out.println("contract : "+CONTRACT_ADDRESS);
        System.out.println("from: "+account);
        Hash txHash = contract.RentMe().sendTransaction(null);//account,new BigInteger("90000"));

    }

    @Override
    public void onClick(View view) {
        if (view == button){
            createAccount();
        } else if(view == tx){
            sendTransaction();
        } else if( view == contract){
            contract();
        }
    }
}
