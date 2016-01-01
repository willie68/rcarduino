package tk.android.rcarduino;

import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.net.Socket;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mebel on 10.11.15.
 * Authored by wklaas on 16.11.15.
 */
public class ArduinoRCController {
    public static final int MAX_CHANNEL_VALUE = 4096;
    public static final int NULL_CHANNEL_VALUE = 2048;
    public static final int MIN_CHANNEL_VALUE = 0;

    private static final int MESSAGE_INDEX_ANALOG_1 = 4;
    private static final int MESSAGE_INDEX_DIGITAL_1 = 12;


    public final static String LOG_TAG = "ArduinoRCController";

    private String hostname;
    private int port;
    private byte[] datagram = new byte[32];
    private Socket socket;
    private HandlerThread connectionHandlerThread;
    private ConnectionHandler connectionHandler;
    private StartActivity startActivity;
    private long lastAnalogTransmit;
    private Timer timer = null;

    public ArduinoRCController(StartActivity startActivity) {
        this.startActivity = startActivity;

        connectionHandlerThread = new HandlerThread("ConnectionThread");
        connectionHandlerThread.start();

        connectionHandler = new ConnectionHandler(startActivity, connectionHandlerThread.getLooper());

        initDatagram();
    }

    public ArduinoRCController(StartActivity startActivity, String hostName) {
        this(startActivity);

        connectionHandler.setHostname(hostname);

        Message message = Message.obtain(connectionHandler, MessageCode.CLASS_CONNECTION, MessageCode.CONNECTION_CONNECT, 0);
        connectionHandler.sendMessage(message);
    }

    public void setHostname(String hostname) {
        connectionHandler.setHostname(hostname);

        Message message = Message.obtain(connectionHandler, MessageCode.CLASS_CONNECTION, MessageCode.CONNECTION_RECONNECT, 0);
        connectionHandler.sendMessage(message);
    }

    public void start() {
        if (timer == null) {
            timer = new Timer("RCBack");
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    synchronized (datagram) {
                        long actualTime = System.currentTimeMillis();
                        if ((actualTime - lastAnalogTransmit) > 900) {
                            transmitMessage();
                            lastAnalogTransmit = actualTime;
                        }
                    }
                }
            }, 1000, 1000);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initDatagram() {
        // RCArduino Message identifier
        datagram[0] = (byte) 0xdf;
        datagram[1] = (byte) 0x81;
        // RCArduino Priority 1 datagram
        datagram[2] = (byte) 0x00;
        datagram[3] = (byte) 0x11;

        for (int i = 4; i < datagram.length; i++) {
            datagram[i] = 0;
        }
    }

    public void switchOn(int switchNumber) {
        Log.i(LOG_TAG, "switch " + switchNumber + " on");

        setSwitch(switchNumber, true);
    }

    public void switchOff(int switchNumber) {
        Log.i(LOG_TAG, "switch " + switchNumber + " off");

        setSwitch(switchNumber, false);
    }

    private void setSwitch(int switchNumber, boolean on) {
        int bitPos = (switchNumber - 1) % 8;
        int bytePos = (switchNumber - 1) / 8;
        if ((bytePos >= 0) && (bytePos <= 7)) {
            synchronized (datagram) {
                byte value = datagram[MESSAGE_INDEX_DIGITAL_1 + bytePos];
                if (on) {
                    value = (byte) (value | (byte) (1 << bitPos));
                } else {
                    value = (byte) (value & ~(1 << bitPos));
                }
                datagram[MESSAGE_INDEX_DIGITAL_1 + bytePos] = value;
                transmitMessage();
            }
        }
    }

    public void setChannel(int channelNumber, int value) {
        if (channelNumber >= 1 && channelNumber <= 4) {
            synchronized (datagram) {
                if (value > MAX_CHANNEL_VALUE) {
                    value = MAX_CHANNEL_VALUE;
                }
                if (value < MIN_CHANNEL_VALUE) {
                    value = MIN_CHANNEL_VALUE;
                }
                int index = (channelNumber - 1) * 2 + MESSAGE_INDEX_ANALOG_1;
                byte highByte = (byte) ((value & 0xFF00) >> 8);
                byte lowByte = (byte) (value & 0x00FF);
                datagram[index] = highByte;
                datagram[index + 1] = lowByte;
                long actualTime = System.currentTimeMillis();
                if ((actualTime - lastAnalogTransmit > 100) || (value == NULL_CHANNEL_VALUE)) {
                    transmitMessage();
                    lastAnalogTransmit = actualTime;
                }
                Log.i(LOG_TAG, "channel " + channelNumber + " " + value);
            }
        }
    }

    private void transmitMessage() {
        byte[] datagramCpy = Arrays.copyOf(datagram, datagram.length);
        datagramCpy = injectCRC16(datagramCpy);

        Message msg = Message.obtain(connectionHandler, MessageCode.SEND_MESSAGE, datagramCpy);
        connectionHandler.sendMessage(msg);
    }

    private byte[] injectCRC16(byte[] localDatagram) {
        byte highCrcByte = 0;
        byte lowCrcByte = 0;
        for (int i = 0; i < 15; i++) {
            highCrcByte = (byte) (highCrcByte ^ localDatagram[i * 2]);
            lowCrcByte = (byte) (lowCrcByte ^ localDatagram[(i * 2) + 1]);
        }
        localDatagram[30] = highCrcByte;
        localDatagram[31] = lowCrcByte;
        return localDatagram;
    }

    ;
}
