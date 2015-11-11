package tk.android.rcarduino.listener;

import android.widget.CompoundButton;

import tk.android.rcarduino.ArduinoRCController;

/**
 * Created by wklaa_000 on 11.11.2015.
 */
public class MyCheckChangeListener implements CompoundButton.OnCheckedChangeListener {

    private int buttonId;
    private ArduinoRCController controller;

    public MyCheckChangeListener(ArduinoRCController controller, int buttonId) {
        this.buttonId = buttonId;
        this.controller = controller;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            controller.switchOn(buttonId);
        } else {
            controller.switchOff(buttonId);
        }
    }

}
