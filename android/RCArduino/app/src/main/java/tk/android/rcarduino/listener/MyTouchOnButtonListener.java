package tk.android.rcarduino.listener;

import android.view.MotionEvent;
import android.view.View;

import tk.android.rcarduino.ArduinoRCController;

/**
 * Created by mebel on 10.11.15.
 */
public class MyTouchOnButtonListener implements View.OnTouchListener {

    private int channelX;
    private int channelY;
    private int oldX;
    private int oldY;
    private ArduinoRCController controller;

    public static int map(int source, int sourceMin, int sourceMax, int destMin, int destMax) {
        return (source - sourceMin) * (destMax - destMin) / (sourceMax - sourceMin) + destMin;
    }

    public MyTouchOnButtonListener(ArduinoRCController controller, int channelX, int channelY) {
        this.channelX = channelX;
        this.channelY = channelY;
        this.controller = controller;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int valueX = ArduinoRCController.NULL_CHANNEL_VALUE;
        int valueY = ArduinoRCController.NULL_CHANNEL_VALUE;

        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            valueX = map(Math.round(x), 0, view.getWidth(), ArduinoRCController.MIN_CHANNEL_VALUE, ArduinoRCController.MAX_CHANNEL_VALUE);
            valueY = map(Math.round(y), 0, view.getHeight(), ArduinoRCController.MIN_CHANNEL_VALUE, ArduinoRCController.MAX_CHANNEL_VALUE);
        }

        if (oldX != valueX) {
            controller.setChannel(channelX, valueX);
            oldX = valueX;
        }
        if (oldY != valueY) {
            controller.setChannel(channelY, valueY);
            oldY = valueY;
        }

        return false;
    }
}
