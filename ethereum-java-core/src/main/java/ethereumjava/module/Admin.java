package ethereumjava.module;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.objects.NodeInfo;
import ethereumjava.module.objects.Peer;
import rx.Observable;

import java.util.List;

/**
 * Created by gunicolas on 17/08/16.
 */

/**
 * Admin module like the one exposed by Geth node.
 * Gives access to methods to manage, control and monitor a node.
 * See specifications at :
 * <p>
 * <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#admin">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#admin</a>
 * <p>
 * It's an interface because calls are made using Java reflection.
 */
public interface Admin {

    /**
     * Pass a <em>nodeURL</em> to connect a to a peer on the network. The <em>nodeURL</em> needs to
     * be in enode URL format (enode://id@ip:port). Node URL can be found by using
     * <i>admin.nodeInfo.enode</i>. Geth will maintain the connection until it shuts down and attempt to
     * reconnect if the connection drops intermittently.
     * <p>
     * <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminaddpeer">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminaddpeer</a>
     *
     * @param peer a node URL in enode URL format (enode://id@ip:port)
     * @return true if peer parameter is well formed and a communication request is sent.
     * @throws EthereumJavaException if geth returns an error or communication shut down.
     */
    boolean addPeer(String peer) throws EthereumJavaException;

    /**
     * Get information on the currently connected node synchronously.
     * <p>
     * <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminnodeinfo">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminnodeinfo</a>
     *
     * @return NodeInfo object containing infos on the node
     * @throws EthereumJavaException if geth returns an error or communication shut down
     * @see NodeInfo
     */
    NodeInfo nodeInfo() throws EthereumJavaException;

    /**
     * Get information on the currently connected node asynchronously. Returned Observable can be
     * subscribed to wait NodeInfo on a different thread. Prevents blocking the main thread.
     * <p>
     * <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminnodeinfo">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminnodeinfo</a>
     *
     * @return an Observable of NodeInfo object containing info on the node.
     * @throws EthereumJavaException if geth returns an error or communication shut down
     * @see NodeInfo
     * @see Observable
     */
    Observable<NodeInfo> getNodeInfo() throws EthereumJavaException;

    /**
     * Get a list of the currently connected peers synchronously
     * <p>
     * <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminpeers">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminpeers</a>
     *
     * @return a list of Peer object, currently connected to the node.
     * @throws EthereumJavaException if geth returns an error or communication shut down
     * @see Peer
     */
    List<Peer> peers() throws EthereumJavaException;

    /**
     * Get a list of the currently connected peers asynchronously Returned Observable can be
     * subscribed to wait connected peers info on a different thread. Prevents blocking the main
     * thread.
     * <p>
     * <a href="https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminpeers">https://github.com/ethereum/go-ethereum/wiki/JavaScript-Console#adminpeers</a>
     *
     * @return an Observable of list of Peer objects, currently connected to the node.
     * @throws EthereumJavaException if geth returns an error or communication shut down
     * @see Peer
     * @see Observable
     */
    Observable<List<Peer>> getPeers() throws EthereumJavaException;

}
