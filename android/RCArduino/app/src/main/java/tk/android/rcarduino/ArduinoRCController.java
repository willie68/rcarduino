package tk.android.rcarduino;

import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.net.Socket;

/**
 * Created by mebel on 10.11.15.
 */
public class ArduinoRCController {
    public static final int MAX_CHANNEL_VALUE = 4096;
    public static final int NULL_CHANNEL_VALUE = 2048;
    public static final int MIN_CHANNEL_VALUE = 0;

    private static final int MESSAGE_INDEX_ANALOG_1 = 4;
    private static final int MESSAGE_INDEX_DIGITAL_1 = 12;


    public final static String LOG_TAG="ArduinoRCController";

    private String hostname;
    private int port;
    private byte[] message = new byte[32];
    private Socket socket;
    private HandlerThread connectionHandlerThread;
    private ConnectionHandler connectionHandler;
    private StartActivity startActivity;

    public ArduinoRCController(StartActivity startActivity, String hostName) {
        this.startActivity = startActivity;
        connectionHandlerThread = new HandlerThread("ConnectionThread");
        connectionHandlerThread.start();

        connectionHandler = new ConnectionHandler(startActivity, connectionHandlerThread.getLooper());

        this.setHostname(hostName);

        Message message = Message.obtain(connectionHandler,MessageCode.CLASS_CONNECTION, MessageCode.CONNECTION_CONNECT, 0);
        connectionHandler.sendMessage(message);
        initMessage();
    }

    public void setHostname(String hostname) {
        connectionHandler.setHostname(hostname);

        Message message = Message.obtain(connectionHandler, MessageCode.CLASS_CONNECTION, MessageCode.CONNECTION_RECONNECT, 0);
        connectionHandler.sendMessage(message);

    }

    private void initMessage() {
        // RCArduino Message identifier
        message[0] = (byte) 0xdf;
        message[1] = (byte) 0x81;
        // RCArduino Prio message
        message[2] = (byte) 0x00;
        message[3] = (byte) 0x18;

        for (int i = 4; i < message.length; i++) {
            message[i] = 0;
        }
    }

    public void switchOn(int switchNumber) {
        Log.i(LOG_TAG, "switch " + switchNumber + " on");
        if ((switchNumber >= 1) && (switchNumber <= 8)){
            byte value = message[MESSAGE_INDEX_DIGITAL_1];
            value = (byte) (value | (byte) (1 << (switchNumber - 1)));
            message[MESSAGE_INDEX_DIGITAL_1] = value;
            transmitMessage();
        }
    }

    public void switchOff(int switchNumber) {
        Log.i(LOG_TAG, "switch " + switchNumber + " off");
        if ((switchNumber >= 1) && (switchNumber <= 8)){
            byte value = message[MESSAGE_INDEX_DIGITAL_1];
            value = (byte) (value & ~ (1 << (switchNumber - 1)));
            message[MESSAGE_INDEX_DIGITAL_1] = value;
            transmitMessage();
        }
    }

    public void setChannel(int channelNumber, int value) {
        if (channelNumber >= 1 && channelNumber <= 4) {
            if (value > MAX_CHANNEL_VALUE) {
                value = MAX_CHANNEL_VALUE;
            }
            if (value < MIN_CHANNEL_VALUE) {
                value = MIN_CHANNEL_VALUE;
            }
            int index = (channelNumber - 1) * 2 + MESSAGE_INDEX_ANALOG_1;
            byte highByte = (byte) ((value & 0xFF00) >> 8);
            byte lowByte = (byte) (value & 0x00FF);
            message[index] = highByte;
            message[index+1] = lowByte;
            transmitMessage();
            Log.i(LOG_TAG, "channel " + channelNumber + " " + value);
        }
    }

    private void transmitMessage() {
        Message msg = Message.obtain(connectionHandler, MessageCode.SEND_MESSAGE, message);
        connectionHandler.sendMessage(msg);
    }
}
