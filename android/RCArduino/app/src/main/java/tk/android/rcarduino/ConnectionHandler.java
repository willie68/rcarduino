package tk.android.rcarduino;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by wklaa_000 on 11.11.2015.
 */
public class ConnectionHandler extends Handler {

    public static final String MESSAGE_DATA = "be.zweetinc.PcController.Message_Data";

    private Socket client;
    private OutputStream outputStream;
    private Message message;
    private String hostname;
    private int port;
    private StartActivity startActivity;
    private boolean hasError;

    public ConnectionHandler(StartActivity startActivity, Looper looper){
        super(looper);
        this.startActivity = startActivity;
        hasError = false;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        message = msg;
        if(message.what != MessageCode.CLASS_CONNECTION){
            sendMessageToServer(msg);
        } else {
            handleConnection();
        }
    }

    private void handleConnection(){
        if(message.arg1 == MessageCode.CONNECTION_CONNECT) {
            makeConnection();
        } else if(message.arg1 == MessageCode.CONNECTION_RECONNECT) {
            reconnect();
        } else {
            closeConnection();
        }
    }

    private void sendMessageToServer(Message msg){
        if (msg.what != MessageCode.SEND_MESSAGE) {
            return;
        }

        if (client == null) {
            makeConnection();
        }

        byte[] message = (byte[]) msg.obj;

        try {
            if (outputStream != null) {
                outputStream.write(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError();
            client = null;
        }
    }

    protected void quit(){
        getLooper().quit();
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
        this.port = 80;
        if (hostname.indexOf(":") >= 0) {
            String portStr = hostname.substring(hostname.indexOf(":")+1);
            try {
                this.port = Integer.parseInt(portStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.hostname = hostname.substring(0, hostname.indexOf(":"));
        }
        hasError = false;
    }

    private void makeConnection(){
        try {
            //InetAddress serverAddr = InetAddress.getByName(hostname);
            client = new Socket();
            client.connect(new InetSocketAddress(hostname, port), 1000);

            Log.d("ConnectionInfo", client.toString());
            outputStream = client.getOutputStream();
            startActivity.changeConnectionState(true);
            hasError = false;
        } catch (IOException e) {
            e.printStackTrace();
            showError();
            client = null;
        }
    }

    private void reconnect() {
        try {
            if (client != null) {
                client.close();
                client = null;
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            showError();
        }
        makeConnection();
    }

    private void closeConnection(){
        try {
            if (client != null) {
                client.close();
                client = null;
                startActivity.changeConnectionState(false);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        quit();
    }

    private void showError() {
        if (!hasError) {
            startActivity.showAlertMessage("Keine Verbindung", String.format("Keine Verbindung zum Empfänger auf \"%s\" möglich!", hostname));
            hasError = true;
        }
        startActivity.changeConnectionState(false);
    }
}
