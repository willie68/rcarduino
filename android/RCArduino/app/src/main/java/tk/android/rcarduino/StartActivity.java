package tk.android.rcarduino;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tk.android.rcarduino.listener.MyButtonClickListener;
import tk.android.rcarduino.listener.MyTouchOnButtonListener;


public class StartActivity extends Activity {

    public final static int LeftPanBtnId = 1000;
    public final static int RightPanBtnId = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        registerToggleButtonListener();
        registerTouchOnButtonListener();


    }

    private void registerTouchOnButtonListener() {
        Button leftBtn = (Button) findViewById(R.id.leftBtn);
        leftBtn.setOnTouchListener(new MyTouchOnButtonListener(LeftPanBtnId));

        Button rightBtn = (Button) findViewById(R.id.leftBtn);
        rightBtn.setOnTouchListener(new MyTouchOnButtonListener(RightPanBtnId));
    }

    private void registerToggleButtonListener() {
        for (int i=0;i<9;i++) {
            String buttonID = "button"+i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setOnClickListener(new MyButtonClickListener(i));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            return rootView;
        }
    }
}
