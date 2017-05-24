package io.ethmobile.ethdroid;

import org.ethereum.geth.Enodes;
import org.ethereum.geth.Geth;
import org.ethereum.geth.NodeConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gunicolas on 16/05/17.
 */

public class ChainConfig {

    private static final long MAINNET_NETWORKID = 1;
    private static final long TESTNET_NETWORDID = 3;

    long networkID;
    String genesis;
    List<String> bootnodes;
    long dbCache;
    boolean ethereumEnabled;
    String monitoringServerURL;
    long maxPeers;
    boolean whisperEnabled;

    NodeConfig nodeConfig;

    private ChainConfig(){
        bootnodes = new ArrayList<>();
        nodeConfig = Geth.newNodeConfig();
    }

    public static ChainConfig getMainnetConfig(){

        ChainConfig config = new Builder( MAINNET_NETWORKID,Geth.mainnetGenesis())
                            .build();
        config.nodeConfig.setBootstrapNodes(Geth.foundationBootnodes());
        return config;
    }

    public static ChainConfig getTestnetConfig(){
       ChainConfig config = new Builder(  TESTNET_NETWORDID, Geth.testnetGenesis())
                            .build();
        config.nodeConfig.setBootstrapNodes(Geth.foundationBootnodes());
        return config;
    }

    public long getNetworkID() {
        return networkID;
    }

    public static class Builder{

        private static final long DB_CACHE_DEFAULT = 16;
        private static final long MAX_PEERS_DEFAULT = 25;
        private static final boolean ETHEREUM_ENABLED_DEFAULT = true;
        private static final boolean WHISPER_ENABLED_DEFAULT = false;
        private static final String NETSTAT_DEFAULT = "";

        private ChainConfig build;

        private Builder(long networkID,String genesis){
            //TODO parse JSON and extract chainID

            build = new ChainConfig();
            withNetworkID(networkID);
            withGenesis(genesis);
            withDbCache(DB_CACHE_DEFAULT);
            withMaxPeerConnection(MAX_PEERS_DEFAULT);

            if(ETHEREUM_ENABLED_DEFAULT) enableEthereum();
            else disableEthereum();

            if(WHISPER_ENABLED_DEFAULT) enableWhisper();
            else disableWhisper();

            monitoredBy(NETSTAT_DEFAULT);
        }

        public Builder(long networkID,String genesis,List<String> bootnodes){
            this(networkID,genesis);
            connectedToBootnodes(bootnodes);
        }

        public Builder(long networkID,String genesis,String bootnode){
            this(networkID,genesis);
            connectedToBootnode(bootnode);
        }

        /**
         * Set the config network id
         * Cf. //TODO
         * @param networkID network identification number
         * @return reference on the parametrized builder
         */
        public Builder withNetworkID(long networkID){
            build.networkID = networkID;
            build.nodeConfig.setEthereumNetworkID(networkID);
            return this;
        }

        /**
         * Set the size of the blockchain database in cache
         * Cf. //TODO
         * @param dbCache
         * @return reference on the parametrized builder
         */
        public Builder withDbCache(long dbCache){
            build.dbCache = dbCache;
            build.nodeConfig.setEthereumDatabaseCache(dbCache);
            return this;
        }

        /**
         * Set the chain genesis in string format.
         * Cf. //TODO
         * @param genesis chain genesis in string format.
         * @return reference on the parametrized builder
         */
        public Builder withGenesis(String genesis){
            build.genesis = genesis;
            build.nodeConfig.setEthereumGenesis(genesis);
            return this;
        }

        /**
         * Set the maximal number of peers the node can connect to.
         * @param maxPeer the maximal number of peers the node can connect to.
         * @return reference on the parametrized builder
         */
        public Builder withMaxPeerConnection(long maxPeer){
            build.maxPeers = maxPeer;
            build.nodeConfig.setMaxPeers(maxPeer);
            return this;
        }

        /**
         * Set the list of bootnodes url in string format.
         * Cf. //TODO
         * @param bootnodeURLList the list of bootnode urls in string format.
         * @return reference on the parametrized builder
         */
        public Builder connectedToBootnodes(List<String> bootnodeURLList){
            build.bootnodes = bootnodeURLList;
            build.nodeConfig.setBootstrapNodes(listToEnodes(bootnodeURLList));
            return this;
        }

        /**
         * Add a bootnode in the current list.
         * Cf. //TODO
         * @param bootnodeURL the bootnode url to add to the current list
         * @return reference on the parametrized builder
         */
        public Builder connectedToBootnode(String bootnodeURL){
            build.bootnodes.add(bootnodeURL);
            return this;
        }

        /**
         * Enable ethereum protocol.
         * Cf. //TODO
         * @return reference on the parametrized builder
         */
        public Builder enableEthereum(){
            build.ethereumEnabled = true;
            build.nodeConfig.setEthereumEnabled(true);
            return this;
        }
        /**
         * Disable ethereum protocol.
         * Cf. //TODO
         * @return reference on the parametrized builder
         */
        public Builder disableEthereum(){
            build.ethereumEnabled = false;
            build.nodeConfig.setEthereumEnabled(false);
            return this;
        }

        /**
         * Enable whisper protocol.
         * Cf. //TODO
         * @return reference on the parametrized builder
         */
        public Builder enableWhisper(){
            build.whisperEnabled = true;
            build.nodeConfig.setWhisperEnabled(true);
            return this;
        }
        /**
         * Disable whisper protocol.
         * Cf. //TODO
         * @return reference on the parametrized builder
         */
        public Builder disableWhisper(){
            build.whisperEnabled = false;
            build.nodeConfig.setWhisperEnabled(false);
            return this;
        }

        /**
         * Set the monitoring server URL (netstat)
         * Cf. //TODO
         * @param monitoringServerURL the monitoring server URL in string format : nodename:secret@host:port
         * @return reference on the parametrized builder
         */
        public Builder monitoredBy(String monitoringServerURL){
            build.monitoringServerURL = monitoringServerURL;
            build.nodeConfig.setEthereumNetStats(monitoringServerURL);
            return this;
        }

        public ChainConfig build(){
            build.nodeConfig.setBootstrapNodes(listToEnodes(build.bootnodes));
            return build;
        }

        private static Enodes listToEnodes(List<String> enodesList){
            int listSize = enodesList.size();
            Enodes enodes = Geth.newEnodes(listSize);
            for(int i=0;i<listSize;i++){
                try {
                    enodes.set(i,Geth.newEnode(enodesList.get(i)));
                } catch (Exception e) {
                    //TODO use logger
                    System.out.println(e.getMessage());
                }
            }
            return enodes;
        }
    }



}
