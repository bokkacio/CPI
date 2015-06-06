package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.event.InitDatabaseEvent;
import ru.iate.cpi.service.CpiService;
import ru.iate.cpi.ui.LogTags;
import ru.iate.cpi.ui.OptionMenuCodes;

import java.util.concurrent.TimeUnit;


/**
 * Created by sanea on 23.03.15.
 */
public class About extends Activity {

    private static final int SLEEP_PERIOD = 25;
    private static final int MAX_PROGRESS = 100;
    private ProgressBar pbAppLoadProcess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        startService(new Intent(this, CpiService.class));
        initComponents();
        showAppLoad();
    }

    @Override
    public void onDestroy(){
        //stop service after first activity was destroyed
        stopService(new Intent(this, CpiService.class));
        super.onDestroy();
    }

    private void initComponents(){
        pbAppLoadProcess = (ProgressBar)findViewById(R.id.pb_appLoadProcess);
    }

    //emulate application loading process
    private void showAppLoad(){
        Thread uiThread = new Thread( new Runnable() {
            public void run() {
                try {
                    for (int i = 1; i <= MAX_PROGRESS; i++)
                    {
                        //init database in the service
                        if(i == MAX_PROGRESS/2)
                            EventBus.getDefault().post(new InitDatabaseEvent());

                        TimeUnit.MILLISECONDS.sleep(SLEEP_PERIOD);
                        pbAppLoadProcess.setProgress(i);
                    }

                    //start Settings activity
                    Intent intent = new Intent(pbAppLoadProcess.getContext(), ru.iate.cpi.ui.activity.Settings.class);
                    startActivityForResult(intent, OptionMenuCodes.SETTINGS_ACTIVITY);

                } catch (InterruptedException e) {
                    Log.d(LogTags.ERROR_PREFIX, e.getMessage());
                }
            }
        });
        uiThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Close app in any way
        finish();
    }
}