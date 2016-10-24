package ethereumjava.net.provider;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ethereumjava.exception.EthereumJavaException;

/**
 * Created by gunicolas on 27/07/16.
 */
public class AndroidIpcProvider extends IpcAbstractProvider {

    LocalSocket socket;

    public AndroidIpcProvider(String _ipcFilePath) throws EthereumJavaException {
        super(_ipcFilePath);
    }

    @Override
    public void init() throws EthereumJavaException {
        try {
            this.socket = new LocalSocket();
            this.socket.connect(new LocalSocketAddress(ipcFilePath, LocalSocketAddress.Namespace.FILESYSTEM));
            this.outputStream = this.socket.getOutputStream();
            this.inputStream = this.socket.getInputStream();
            this.in = new BufferedReader(new InputStreamReader(this.inputStream));
            this.out = new DataOutputStream(this.outputStream);
            super.init();
        } catch (IOException e) {
            throw new EthereumJavaException(e);
        }

    }

    @Override
    public void stop() throws EthereumJavaException {
        if( this.socket != null ) {
            try {
                this.socket.close();
            } catch (IOException e) {
                throw new EthereumJavaException(e);
            }
        }
        super.stop();
    }
}
