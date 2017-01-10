package ethereumjava.config;

import ethereumjava.EthereumJava;
import ethereumjava.net.provider.RpcProvider;
import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by gunicolas on 12/10/16.
 */
public class RPCTest {

    protected EthereumJava ethereumJava;
    protected Config config;
    protected Process p;

    @Before
    public void setUp() throws Exception {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ProcessBuilder builder = new ProcessBuilder("/bin/bash", System.getProperty("user.dir") + "/src/test/resources/prerequisite.sh");
                    builder.directory(new File(System.getProperty("user.dir") + "/src/test/resources/"));
                    builder.redirectOutput(new File(System.getProperty("user.dir") + "/src/test/resources/out.log"));
                    builder.redirectError(new File(System.getProperty("user.dir") + "/src/test/resources/out.err.log"));
                    p = builder.start();

                } catch (IOException e) {
                    assertTrue("Prerequisite couldn't be executed" + e, false);
                }
            }
        }).start();

        Thread.sleep(1000);

        BufferedReader br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/src/test/resources/out.err.log")));
        String line;
        boolean keepReading = true;
        while (keepReading) {
            line = br.readLine();
            if (line == null) {
                Thread.sleep(500);
            } else {
                if (line.contains("HTTP endpoint opened")) {
                    keepReading = false;
                }
            }
        }


        config = Config.newInstance();

        ethereumJava = new EthereumJava.Builder()
            .provider(new RpcProvider("http://" + config.rpcProviderAddr + ":" + config.rpcProviderPort))
            .build();

    }

    @After
    public void after() throws Exception {
        ethereumJava.close();

        String killer = System.getProperty("user.dir") + "/src/test/resources/killPrerequisite.sh";

        if (new File(killer).exists()) {
            p.destroy();
            while (new File(killer).exists()) {
                // DO NOTHING
            }
        }
    }
}
