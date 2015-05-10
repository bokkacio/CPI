package ru.iate.cpi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.db.DatabaseFactory;
import ru.iate.cpi.db.manager.RegionManager;
import ru.iate.cpi.event.InitDatabaseEvent;

/**
 * Created by sanea on 04.05.15.
 */
public class CpiService extends Service {
    public static String ERROR_PREFIX = "cpi_error";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DatabaseFactory.Release();
    }

    //init database
    public void onEventBackgroundThread(InitDatabaseEvent event){
        DatabaseFactory.Set(getApplicationContext());
        RegionManager manager = new RegionManager(getApplicationContext(), DatabaseFactory.Get());

        //fill regions only once
        if(manager.GetRegions().isEmpty())
            manager.FillRegions();
    }
}
