package ethereumjava;

import ethereumjava.module.objects.Block;
import ethereumjava.module.objects.Hash;
import ethereumjava.module.objects.TransactionRequest;
import ethereumjava.net.Request;
import ethereumjava.net.provider.Provider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx.Observable;
import rx.Subscriber;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gunicolas on 24/08/16.
 */
public class InvocationHandlerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    Provider mockProvider;
    @Captor
    ArgumentCaptor<Request> captor;
    private EthereumJava ethereumJava;

    @Before
    public void setup() {
        ethereumJava = new EthereumJava.Builder()
            .provider(mockProvider)
            .build();
    }

    @Test
    public void synchronousMethodReturnedObjectTest() throws Exception {
        final Block block = new Block();
        Observable.OnSubscribe subscriber = new Observable.OnSubscribe<Block>() {
            @Override
            public void call(Subscriber<? super Block> subscriber) {
                subscriber.onNext(block);
                subscriber.onCompleted();
            }
        };
        Observable observable = Observable.create(subscriber);
        when(mockProvider.sendRequest(any(Request.class))).thenReturn(observable);
        Observable<Block<Hash>> b = ethereumJava.eth.getBlock(Hash.valueOf(""), Hash.class);
        b.subscribe(new Subscriber<Block>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Block returnedBlock) {
                assertEquals(block, returnedBlock);
            }
        });
    }

    @Test
    public void asynchronousMethodReturnedObjectTest() throws Exception {
        Block block = new Block();
        Observable observable = Observable.just(block);
        when(mockProvider.sendRequest(any(Request.class))).thenReturn(observable);

        Block<Hash> returnedBlock = ethereumJava.eth.block(Hash.valueOf(""), Hash.class);
        assertTrue(returnedBlock.equals(block));
    }

    @Test
    public void synchronousMethodRequestTest() throws Exception {
        ethereumJava.eth.getBlock(Hash.valueOf("0x63cb70cdcef14c3ba7572b5171cf09df2bc7685a8e790588260a1396856c2482"), Hash.class);
        verify(mockProvider).sendRequest(captor.capture());
        Request req = captor.<Request>getValue();
        /*Type type = new TypeToken<Block<Hash>>(){}.getType();
        assertEquals(type,req.getReturnType());*/ //TODO resolve
        assertEquals("eth_getBlockByHash", req.getMethodCall());
        assertEquals("[\"0x63cb70cdcef14c3ba7572b5171cf09df2bc7685a8e790588260a1396856c2482\",false]", req.getArguments());

    }

    @Test
    public void asynchronousMethodRequestTest() throws Exception {
        ethereumJava.eth.getBlock(Hash.valueOf("0x63cb70cdcef14c3ba7572b5171cf09df2bc7685a8e790588260a1396856c2482"), Hash.class);
        verify(mockProvider).sendRequest(captor.capture());
        Request req = captor.<Request>getValue();
       /* Type type = new TypeToken<Block<Hash>>(){}.getType();
        assertEquals(type,req.getReturnType());*/ //TODO resolve
        assertEquals("eth_getBlockByHash", req.getMethodCall());
        assertEquals("[\"0x63cb70cdcef14c3ba7572b5171cf09df2bc7685a8e790588260a1396856c2482\",false]", req.getArguments());
        assertEquals(0, req.getId());
        assertEquals(new ArrayList<Subscriber>(), req.getSubscribers());

    }

    @Test
    public void asynchronousMethodRequestBigIntegerTest() throws Exception {
        ethereumJava.eth.getBlock(new BigInteger("0"), Hash.class);
        verify(mockProvider).sendRequest(captor.capture());
        Request req = captor.<Request>getValue();
        /*Type type = new TypeToken<Block<Hash>>(){}.getType();
        assertEquals(type,req.getReturnType());*/ //TODO resolve
        assertEquals("eth_getBlockByNumber", req.getMethodCall());
        assertEquals("[\"0x0\",false]", req.getArguments());
        assertEquals(0, req.getId());
        assertEquals(new ArrayList<Subscriber>(), req.getSubscribers());
    }

    @Test
    public void sendTransactionTest() throws Exception {
        String from = "0xf1e04ff9007ee1e0864cd39270a407c71b14b7e2";
        String to = "0xf1e04ff9007ee1e0864cd39270a407c71b14b7e2";
        String value = "545";
        String data = "hello";

        TransactionRequest t = new TransactionRequest(from, to, value, data);
        ethereumJava.eth.sendTransaction(t);

        verify(mockProvider).sendRequest(captor.capture());
        Request req = captor.<Request>getValue();

        assertEquals(Hash.class, req.getReturnType());
        assertEquals("eth_sendTransaction", req.getMethodCall());
        assertEquals("[" + t.toString() + "]", req.getArguments());
        assertEquals(0, req.getId());
        assertEquals(new ArrayList<Subscriber>(), req.getSubscribers());

    }
}
