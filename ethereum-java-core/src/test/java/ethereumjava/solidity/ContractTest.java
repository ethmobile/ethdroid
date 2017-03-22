package ethereumjava.solidity;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import ethereumjava.config.Config;
import ethereumjava.config.RPCTest;
import ethereumjava.module.objects.Transaction;
import ethereumjava.solidity.element.function.SolidityFunction2;
import ethereumjava.solidity.element.returns.PairReturn;
import ethereumjava.solidity.element.returns.SingleReturn;
import ethereumjava.solidity.element.returns.TripleReturn;
import ethereumjava.solidity.types.SArray;
import ethereumjava.solidity.types.SBool;
import ethereumjava.solidity.types.SInt;
import ethereumjava.solidity.types.SType;
import ethereumjava.solidity.types.SUInt;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by gunicolas on 30/08/16.
 */
public class ContractTest extends RPCTest {

    ITestContract contract;
    Config.TestAccount testAccount;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        testAccount = config.accounts.get(1);
        ethereumJava.personal.unlockAccount(testAccount.id, testAccount.password, 3600);
        contract = ethereumJava.contract.withAbi(ITestContract.class).at(config.contractAddress);
    }

    @Test
    public void testEventReturnsUInt() throws Exception {

        TestSubscriber<SingleReturn<SUInt.SUInt256>> testSubscriber = new TestSubscriber<>();
        contract.testEventReturnsUInt().watch().first().subscribe(testSubscriber);

        contract.throwEventReturnsUInt().sendTransaction(testAccount.id, new BigInteger("90000"));

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        List<SingleReturn<SUInt.SUInt256>> states = testSubscriber.getOnNextEvents();
        int state = states.get(0).getElement1().get().intValue();
        Assert.assertTrue(state == 2);
    }

    @Test
    public void testEventReturnsBool() throws Exception {

        TestSubscriber<SingleReturn<SBool>> testSubscriber = new TestSubscriber<>();
        contract.testEventReturnsBool().watch().first().subscribe(testSubscriber);

        contract.throwEventReturnsBool().sendTransaction(testAccount.id, new BigInteger("90000"));

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        List<SingleReturn<SBool>> states = testSubscriber.getOnNextEvents();
        boolean state = states.get(0).getElement1().get();
        Assert.assertTrue(state);
    }

    @Test
    public void testEventReturnsMatrix() throws Exception {

        TestSubscriber<SingleReturn<SArray<SArray<SInt.SInt256>>>> testSubscriber = new TestSubscriber<>();
        contract.testEventReturnsMatrix().watch().first().subscribe(testSubscriber);

        contract.throwEventReturnsMatrix().sendTransaction(testAccount.id, new BigInteger("90000"));

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        List<SingleReturn<SArray<SArray<SInt.SInt256>>>> states = testSubscriber.getOnNextEvents();
        SingleReturn<SArray<SArray<SInt.SInt256>>> received = states.get(0);

        SArray expected = SArray.fromArray(new SArray[]{
            SArray.fromArray(new SInt.SInt256[]{
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(0)),
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(1)),
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(2))}),
            SArray.fromArray(new SInt.SInt256[]{
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(2)),
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(1)),
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(1))}),
            SArray.fromArray(new SInt.SInt256[]{
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(2)),
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(0)),
                SInt.SInt256.fromBigInteger256(BigInteger.valueOf(1))})
        });

        Assert.assertTrue(expected.equals(received.getElement1()));

    }

    @Test
    public void testEventReturnsMultiple() throws Exception{
        TestSubscriber<TripleReturn<SBool,SBool,SBool>> testSubscriber = new TestSubscriber<>();
        contract.testEventReturnsMultiple()
            .watch()
            .first()
            .subscribe(testSubscriber);

        contract.throwEventReturnsMultiple().sendTransaction(testAccount.id, new BigInteger("90000"));

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        List<TripleReturn<SBool,SBool,SBool>> states = testSubscriber.getOnNextEvents();
        TripleReturn<SBool,SBool,SBool> received = states.get(0);

        Assert.assertTrue(received.getElement1().get());
        Assert.assertFalse(received.getElement2().get());
        Assert.assertTrue(received.getElement3().get());
    }

    @Test
    public void testFunctionOutputsVoid() throws Exception{
        SType s = contract.testFunctionOutputsVoid().call().getElement1();
        Assert.assertTrue(s == null);
    }

    @Test
    public void testFunctionOutputsBool() throws Exception{
        SBool got = contract.testFunctionOutputsBool().call().getElement1();
        Assert.assertTrue(got.get());
    }

    @Test
    public void testFunctionOutputsMatrix() throws Exception{
        SArray<SArray<SUInt.SUInt8>> got = contract.testFunctionOutputsMatrix().call().getElement1();

        SArray<SArray> expected = SArray.fromArray(new SArray[]{
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 0),
                SUInt.SUInt8.fromShort((short) 1),
                SUInt.SUInt8.fromShort((short) 2)
            }),
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 2),
                SUInt.SUInt8.fromShort((short) 1),
                SUInt.SUInt8.fromShort((short) 1)
            }),
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 2),
                SUInt.SUInt8.fromShort((short) 0),
                SUInt.SUInt8.fromShort((short) 1)
            })
        });

        Assert.assertTrue(got.equals(expected));
    }

    @Test
    public void testFunctionOutputsPrimitive() throws Exception{
        SUInt.SUInt256 got = contract.testFunctionOutputsPrimitive().call().getElement1();
        SUInt.SUInt256 expected = SUInt.fromBigInteger256(BigInteger.valueOf(3));
        Assert.assertTrue(got.equals(expected));
    }

    @Test
    public void testFunctionOutputs2() throws Exception{
        PairReturn<SBool,SBool> got = contract.testFunctionOutputs2().call();
        Assert.assertTrue(got.getElement1().get());
        Assert.assertFalse(got.getElement2().get());
    }

    @Test
    public void testFunctionOutputs3Matrix() throws Exception{
        TripleReturn<SBool,SArray<SArray<SUInt.SUInt8>>,SBool> got = contract.testFunctionOutputs3Matrix().call();

        SArray<SArray> expectedMatrix = SArray.fromArray(new SArray[]{
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 0),
                SUInt.SUInt8.fromShort((short) 1)
            }),
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 2),
                SUInt.SUInt8.fromShort((short) 1)
            }),
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 0),
                SUInt.SUInt8.fromShort((short) 1)
            })
        });

        Assert.assertTrue(got.getElement1().get());
        Assert.assertTrue(got.getElement2().equals(expectedMatrix));
        Assert.assertFalse(got.getElement3().get());
    }

    @Test
    public void testFunctionInputsPrimitives() throws Exception{
        TestSubscriber subscriber = new TestSubscriber();

        Observable<Transaction> obs = contract.testFunctionInputsPrimitives(SUInt.fromBigInteger256(BigInteger.ONE),SUInt.fromBigInteger256(BigInteger.ZERO))
            .sendTransactionAndGetMined(testAccount.id, new BigInteger("90000"));

        obs.subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertCompleted();

    }

    @Test
    public void testFunctionInputsArray() throws Exception{

        TestSubscriber subscriber = new TestSubscriber();

        Observable<Transaction> obs = contract.testFunctionInputsArray(
            SArray.fromArray(new SUInt.SUInt8[]{
                SUInt.SUInt8.fromShort((short) 1),
                SUInt.SUInt8.fromShort((short) 2),
                SUInt.SUInt8.fromShort((short) 3)
            })).sendTransactionAndGetMined(testAccount.id, new BigInteger("90000"));

        obs.subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertCompleted();

    }

}
