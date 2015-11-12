package tk.android.rcarduino;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by mebel on 10.11.15.
 */
public class SettingsActivity  extends Activity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start);

        Button button = (Button)findViewById(R.id.btnSaveSettings);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = SettingsActivity.this.getHostNameTF().getText().toString();
                Settings.saveString(SettingsActivity.this.getApplicationContext(),Settings.KEY_HOSTNAME,value);
                setResult(RESULT_OK);
                finish();
            }
        });
        String hostName = Settings.getString(this.getApplicationContext(),Settings.KEY_HOSTNAME,"192.168.0.1:3456");
        getHostNameTF().setText(hostName);
    }

    public EditText getHostNameTF() {
        EditText tf = (EditText)findViewById(R.id.hostName);
        return tf;
    }
}
