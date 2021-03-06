package tk.android.rcarduino;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import tk.android.rcarduino.listener.MyCheckChangeListener;
import tk.android.rcarduino.listener.MyTouchOnButtonListener;


public class StartActivity extends Activity {

    public final static int LeftPanBtnId = 1000;
    public final static int RightPanBtnId = 1001;
    public final static int REQUEST_CODE_CHANGE_HOST = 4;
    private ArduinoRCController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        controller = new ArduinoRCController(this);

        registerToggleButtonListener();
        registerTouchOnButtonListener();
        registerTitleContainerListener();

        setNewHostname();
    }

    @Override
    protected void onStart() {
        Log.i("Startactivity", "onStart");
        super.onStart();
        controller.start();
    }

    @Override
    protected void onStop() {
        Log.i("Startactivity", "onStop");
        controller.stop();
        super.onStop();
    }

    public void showAlertMessage(final String title, final String text) {
        int resID = getResources().getIdentifier("container", "id", getPackageName());
        final View view = (View) findViewById(resID);
        view.post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(view.getContext()).setTitle(title)
                        .setMessage(text)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    public void changeConnectionState(final boolean connected) {
        int resID = getResources().getIdentifier("connectState", "id", getPackageName());
        final Button button = (Button) findViewById(resID);
        button.post(new Runnable() {
            @Override
            public void run() {
                button.setPressed(connected);
            }
        });
    }

    private void registerTitleContainerListener() {
        View titleContainer = (View) findViewById(R.id.titleContainer);
        titleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, SettingsActivity.class);
                startActivityForResult(i, REQUEST_CODE_CHANGE_HOST);
            }
        });
    }

    private void registerTouchOnButtonListener() {
        Button leftBtn = (Button) findViewById(R.id.leftBtn);
        leftBtn.setOnTouchListener(new MyTouchOnButtonListener(controller, 2, 1));

        Button rightBtn = (Button) findViewById(R.id.rightBtn);
        rightBtn.setOnTouchListener(new MyTouchOnButtonListener(controller, 3, 4));
    }

    private void registerToggleButtonListener() {
        for (int i=1;i<=16;i++) {
            String buttonID = "button"+i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ToggleButton button = (ToggleButton) findViewById(resID);
            if (button!=null) {
                button.setOnCheckedChangeListener(new MyCheckChangeListener(controller, i));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent i = new Intent(StartActivity.this, SettingsActivity.class);
            startActivityForResult(i, REQUEST_CODE_CHANGE_HOST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHANGE_HOST) {
            if (resultCode == RESULT_OK) {
                setNewHostname();
            }
        }
    }

    private void setNewHostname() {
        String hostname = Settings.getString(this.getApplicationContext(),Settings.KEY_HOSTNAME,"192.168.0.1:3456");
        controller.setHostname(hostname);
        TextView textView = (TextView) findViewById(R.id.titleText);
        String mystring = getResources().getString(R.string.app_title);
        textView.setText(String.format(mystring, hostname));
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            return rootView;
        }
    }
}
