package ethereumjava.module.objects;

/**
 * Created by gunicolas on 18/08/16.
 */
public class NodeInfo {

    public String enode;
    public String id;
    public String ip;
    public String listenAddr;
    public String name;
    public Ports ports;
    public Protocols protocols;

    @Override
    public String toString() {
        return "NodeInfo{" +
            "enode='" + enode + '\'' +
            ", id='" + id + '\'' +
            ", ip='" + ip + '\'' +
            ", listenAddr='" + listenAddr + '\'' +
            ", name='" + name + '\'' +
            ", ports=" + ports +
            ", protocols=" + protocols +
            '}';
    }

    public class Protocols {

        public Eth eth;

        @Override
        public String toString() {
            return "Protocols{" +
                "eth=" + eth +
                '}';
        }

        public class Eth {
            public String difficulty;
            public String genesis;
            public String head;
            public int network;

            @Override
            public String toString() {
                return "Eth{" +
                    "difficulty=" + difficulty +
                    ", genesis='" + genesis + '\'' +
                    ", head='" + head + '\'' +
                    ", network=" + network +
                    '}';
            }
        }
    }

    public class Ports {
        public int discovery;
        public int listener;

        @Override
        public String toString() {
            return "Ports{" +
                "discovery=" + discovery +
                ", listener=" + listener +
                '}';
        }
    }
}
