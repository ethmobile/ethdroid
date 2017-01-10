package ethereumjava.solidity;

import ethereumjava.config.Config;
import ethereumjava.config.RPCTest;
import ethereumjava.module.objects.Hash;
import ethereumjava.module.objects.Transaction;
import ethereumjava.solidity.types.SUInt;
import ethereumjava.solidity.types.SVoid;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by gunicolas on 30/08/16.
 */
public class ContractTest extends RPCTest {

    TestContract contract;
    Config.TestAccount testAccount;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testAccount = config.accounts.get(1);
        ethereumJava.personal.unlockAccount(testAccount.id, testAccount.password, 3600);
        contract = ethereumJava.contract.withAbi(TestContract.class).at(config.contractAddress);
    }

    @Test
    public void testContractRentMe() throws Exception {
        Hash txHash = rentMe();
        assertTrue(txHash != null);
    }

    private Hash rentMe() {
        return contract.RentMe().sendTransaction(testAccount.id, new BigInteger("90000"));
    }

    @Test
    public void testContractStopRent() throws Exception {
        Hash txHash = stopRent();
        assertTrue(txHash != null);
    }

    private Hash stopRent() {
        return contract.StopRent().sendTransaction(testAccount.id, new BigInteger("90000"));
    }

    @Test
    public void testContractStartRent() throws Exception {
        Hash txHash = startRent();
        assertTrue(txHash != null);
    }

    private Hash startRent() {
        return contract.StartRent().sendTransaction(testAccount.id, new BigInteger("90000"), SolidityUtils.toWei("1", "ether").toBigInteger());
    }

    @Test
    public void testContractValidateTravel() throws Exception {
        Hash txHash = validateTravel();
        assertTrue(txHash != null);
    }

    private Hash validateTravel() {
        return contract.ValidateTravel().sendTransaction(testAccount.id, new BigInteger("90000"));
    }

    @Test
    public void testContractGoTo() throws Exception {

        Hash txHash = goTo();

        assertTrue(txHash != null);

    }

    private Hash goTo() {
        return contract
            .GoTo(SUInt.fromBigInteger256(BigInteger.valueOf(3)),
                SUInt.fromBigInteger256(BigInteger.valueOf(2)))
            .sendTransaction(testAccount.id, new BigInteger("90000"));
    }

    @Test
    public void testContractGetPrice() throws Exception {

        TestSubscriber testSubscriber1 = new TestSubscriber();
        TestSubscriber testSubscriber2 = new TestSubscriber();


        Observable<Transaction> obs1 = contract.RentMe()
            .sendTransactionAndGetMined(testAccount.id, new BigInteger("90000"));

        obs1.subscribe(testSubscriber1);

        testSubscriber1.awaitTerminalEvent();


        Observable<Transaction> obs2 = contract.GoTo(SUInt.fromBigInteger256(BigInteger.valueOf(3)),
            SUInt.fromBigInteger256(BigInteger.valueOf(2)))
            .sendTransactionAndGetMined(testAccount.id, new BigInteger("90000"));

        obs2.subscribe(testSubscriber2);

        testSubscriber2.awaitTerminalEvent();

        SUInt.SUInt256 response = getPrice();
        Assert.assertEquals(new BigInteger("1000000000000000000"), response.get());

    }

    private SUInt.SUInt256 getPrice() {
        return contract.GetPrice().call();
    }

    @Test
    public void onStateChangedTest() throws Exception {

        TestSubscriber testSubscriber = new TestSubscriber();
        contract.OnStateChanged().watch().first().subscribe(testSubscriber);

        rentMe();

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        List<SUInt.SUInt256> states = testSubscriber.getOnNextEvents();
        int state = states.get(0).get().intValue();
        Assert.assertTrue(state == 1);
    }

    @Test
    public void sendTransactionAndGetMinedTest() throws Exception {
        TestSubscriber testSubscriber = new TestSubscriber();

        Observable<Transaction> obs = contract.RentMe().sendTransactionAndGetMined(testAccount.id, new BigInteger("90000"));
        obs.subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertTrue(testSubscriber.getOnNextEvents().get(0) != null);

    }

    interface TestContract extends ContractType {

        @SolidityEvent.Anonymous(false)
        @SolidityEvent.Parameters({
            @SolidityEvent.Parameter(indexed = false, name = "state", type = SUInt.SUInt256.class)
        })
        SolidityEvent<SUInt.SUInt256> OnStateChanged();

        @SolidityFunction.ReturnType(SVoid.class)
        SolidityFunction<SVoid> RentMe();

        @SolidityFunction.ReturnType(SVoid.class)
        SolidityFunction<SVoid> StopRent();

        @SolidityFunction.ReturnType(SVoid.class)
        SolidityFunction<SVoid> StartRent();

        @SolidityFunction.ReturnType(SVoid.class)
        SolidityFunction<SVoid> ValidateTravel();

        @SolidityFunction.ReturnType(SVoid.class)
        SolidityFunction<SVoid> GoTo(SUInt.SUInt256 x, SUInt.SUInt256 y);

        @SolidityFunction.ReturnType(SUInt.SUInt256.class)
        SolidityFunction<SUInt.SUInt256> GetPrice();


    }

}
