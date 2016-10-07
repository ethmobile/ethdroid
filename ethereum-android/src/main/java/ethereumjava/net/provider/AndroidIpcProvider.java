package ethereumjava.net.provider;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.IOException;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.net.provider.IpcAbstractProvider;

/**
 * Created by gunicolas on 27/07/16.
 */
public class AndroidIpcProvider extends IpcAbstractProvider {

    LocalSocket socket;

    public AndroidIpcProvider(String _ipcFilePath) throws EthereumJavaException {
        super(_ipcFilePath);
    }

    @Override
    protected void setStreams() throws IOException {
        this.socket = new LocalSocket();
        this.socket.connect(new LocalSocketAddress(ipcFilePath, LocalSocketAddress.Namespace.FILESYSTEM));
        this.outputStream = this.socket.getOutputStream();
        this.inputStream = this.socket.getInputStream();
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
