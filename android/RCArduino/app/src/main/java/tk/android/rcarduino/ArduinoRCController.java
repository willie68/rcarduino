package tk.android.rcarduino;

import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by mebel on 10.11.15.
 */
public class ArduinoRCController {
    public final static String LOG_TAG="ArduinoRCController";

    public static void toggleButton(int buttonId) {
        // #TODO implement something here
        Log.i(LOG_TAG,"toggleButton:" + buttonId);
    }

    public static void setPan(int buttonId, float x, float y) {
        // #TODO implement something here
        Log.i(LOG_TAG,"panButton:" + buttonId + " x:"+x+ " y:"+y);
    }
}
