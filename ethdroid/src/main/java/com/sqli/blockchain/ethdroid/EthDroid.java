package com.sqli.blockchain.ethdroid;

import org.ethereum.geth.Context;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Node;

/**
 * Created by gunicolas on 16/05/17.
 */

public class EthDroid {

    private String datadir;
    private Context mainContext;
    private Node node;
    private ChainConfig chainConfig;

    private EthDroid(){

    }

    public void start() throws Exception {
        node.start();
    }

    public static class Builder {

        EthereumAndroid build;

        /**
         * Parameterized Builder with the default values :
         * - Context : @withDefaultContext
         */
        public Builder(String datadir) {
            build = new EthereumAndroid();
            withDefaultContext();
            withDatadirPath(datadir);
        }

        /**
         * Release resources when execution is over.
         * @return reference on the parametrized builder
         */
        public Builder withDefaultContext(){
            build.mainContext = Geth.newContext();
            return this;
        }
        /**
         * Release resources when execution is over or when a cancel is asked
         * @return reference on the parametrized builder
         */
        public Builder withCancelContext(){
            build.mainContext = Geth.newContext().withCancel();
            return this;
        }
        /**
         * Release resources when execution is over or when it reach the deadline (golang context).
         * @param seconds //TODO
         * @param nanoseconds //TODO
         * @return reference on the parametrized builder
         */
        public Builder withDeadlineContext(long seconds,long nanoseconds){
            build.mainContext = Geth.newContext().withDeadline(seconds,nanoseconds);
            return this;
        }
        /**
         * Release resources when execution is over or when @seconds passed since the function call.
         * @param seconds number of seconds to wait before canceling the call
         * @return reference on the parametrized builder
         */
        public Builder withTimeoutContext(long seconds){
            build.mainContext = Geth.newContext().withTimeout(seconds);
            return this;
        }

        /**
         * Set path where node files will be saved (node key, blockchain db, ...)
         * @param datadir string path where to save node files
         * @return reference on the parametrized builder
         */
        public Builder withDatadirPath(String datadir){
            build.datadir = datadir;
            return this;
        }

        public Builder onMainnet(){
            build.chainConfig = ChainConfig.getMainnetConfig();
            return this;
        }

        public Builder onTestnet(){
            build.chainConfig = ChainConfig.getTestnetConfig();
            return this;
        }

        public Builder withChainConfig(ChainConfig chainConfig){
            build.chainConfig = chainConfig;
            return this;
        }

        public EthereumAndroid build(){
            return build;
        }

    }
}
