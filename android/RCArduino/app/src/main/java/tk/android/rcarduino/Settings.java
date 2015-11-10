package tk.android.rcarduino;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mebel on 10.11.15.
 */
public class Settings {
    public static final String KEY_HOSTNAME = "hostname";

    public static void saveString(SettingsActivity activity, String key, String value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getString(SettingsActivity activity, String key,String defaultStr) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return mPrefs.getString(key,defaultStr);
    }
}
