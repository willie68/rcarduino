package tk.android.rcarduino.listener;

import android.view.MotionEvent;
import android.view.View;

import tk.android.rcarduino.ArduinoRCController;

/**
 * Created by mebel on 10.11.15.
 */
public class MyTouchOnButtonListener implements View.OnTouchListener {

    private int buttonId;


    public MyTouchOnButtonListener(int buttonId)
    {
        this.buttonId = buttonId;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float offsetX = view.getMeasuredWidth() * 0.5f;
        float offsetY = view.getMeasuredHeight() * 0.5f;

        float x = (motionEvent.getX() - offsetX);
        float y = (motionEvent.getY() - offsetY);


        // clamp values between -1 .. 1

        x = -1 + (2 / view.getMeasuredWidth()) * x;
        y = -1 + (2 / view.getMeasuredHeight()) * y;

        ArduinoRCController.setPan(buttonId, x, y);

        return false;

    }
}
