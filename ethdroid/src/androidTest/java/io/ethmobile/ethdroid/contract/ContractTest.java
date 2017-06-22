package io.ethmobile.ethdroid.contract;

import static io.ethmobile.ethdroid.Utils.deleteDirIfExists;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.io.File;

import io.ethmobile.ethdroid.ChainConfig;
import io.ethmobile.ethdroid.EthDroid;

/**
 * Created by gunicolas on 17/05/17.
 */

//@RunWith(AndroidJUnit4.class)
public class ContractTest {

    ITestContract contractInstance;
    private Context appContext;
    private EthDroid ethdroid;

    //@Before
    public void setUp() throws Exception {

        appContext = InstrumentationRegistry.getTargetContext();
        String datadir = appContext.getFilesDir().getAbsolutePath();

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

        ethdroid = new EthDroid.Builder(datadir)
            .withDefaultContext()
            .withChainConfig(new ChainConfig.Builder(networkID, genesis, enode).build())
            .build();

        ethdroid.start();
    }

    //@Test
    public void newInstanceTest() throws Exception {
        contractInstance = ethdroid.getContractInstance(ITestContract.class, "");
    }

    //@Test
    public void testEventReturnsUInt() throws Exception {
        newInstanceTest();
        contractInstance.testEventReturnsUInt();
    }

    //@Test
    public void testEventReturnsBool() throws Exception {
    }

    //@Test
    public void testEventReturnsMatrix() throws Exception {
    }

    //@Test
    public void testEventReturnsMultiple() throws Exception {
    }

    //@Test
    public void testFunctionOutputsVoid() throws Exception {
    }

    //@Test
    public void testFunctionOutputsBool() throws Exception {
    }

    //@Test
    public void testFunctionOutputsMatrix() throws Exception {
    }

    //@Test
    public void testFunctionOutputsPrimitive() throws Exception {
    }

    //@Test
    public void testFunctionOutputs2() throws Exception {
    }

    //@Test
    public void testFunctionOutputs3Matrix() throws Exception {
    }

    //@Test(expected = SmartContractException.class )
    public void testFunctionThrowsException() throws Exception {
    }

    //@Test
    public void testFunctionInputsPrimitives() throws Exception {
    }

    //@Test
    public void testFunctionInputsArray() throws Exception {

    }
}
