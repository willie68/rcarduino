package tk.android.rcarduino.listener;

import android.view.View;

import tk.android.rcarduino.ArduinoRCController;

/**
 * We have to notice a button id. So we create our own Button Click listener
 */
public class MyButtonClickListener implements View.OnClickListener {

    private int buttonId;

    public MyButtonClickListener(int buttonId) {

        this.buttonId = buttonId;
    }

    @Override
    public void onClick(View view) {
        ArduinoRCController.toggleButton(buttonId);
    }
}
