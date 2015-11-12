package tk.android.rcarduino;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mebel on 10.11.15.
 */
public class Settings {
    public static final String KEY_HOSTNAME = "hostname";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key,String defaultStr) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(key,defaultStr);
    }
}
