package tk.android.rcarduino.listener;

import android.view.MotionEvent;
import android.view.View;

import tk.android.rcarduino.ArduinoRCController;

/**
 * Created by mebel on 10.11.15.
 */
public class MyTouchOnButtonListener implements View.OnTouchListener {

    private int buttonId;

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }


    public MyTouchOnButtonListener(int buttonId)
    {
        this.buttonId = buttonId;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        float x = motionEvent.getX();
        float y = motionEvent.getY();


        // clamp values between -1 .. 1

        x = -1.0f + (2.0f / view.getWidth()) * x;
        y = -1.0f + (2.0f / view.getHeight()) * y;

        x = clamp(x,-1.0f,1.0f);
        y = clamp(y,-1.0f,1.0f);

        ArduinoRCController.setPan(buttonId, x, y);

        return false;

    }
}
