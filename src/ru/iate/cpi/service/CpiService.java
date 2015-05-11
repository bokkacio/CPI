package ru.iate.cpi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.db.DatabaseFactory;
import ru.iate.cpi.db.manager.RegionManager;
import ru.iate.cpi.event.GetRegionsEvent;
import ru.iate.cpi.event.InitDatabaseEvent;
import ru.iate.cpi.event.RegionsSourceEvent;
import ru.iate.cpi.ui.LogTags;

/**
 * Created by sanea on 04.05.15.
 */
public class CpiService extends Service {
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
        try {
            DatabaseFactory.Set(getApplicationContext());
            RegionManager manager = new RegionManager(getApplicationContext(), DatabaseFactory.Get());
            //fill regions only once
            if(manager.GetRegions().isEmpty())
                manager.FillRegions();
        }
        catch (Exception ex){
            Log.d(LogTags.ERROR_PREFIX, "CpiService - InitDatabaseEvent" + ex.getMessage());
        }
    }

    //extract regions
    public void onEventBackgroundThread(GetRegionsEvent event){
        try {

            RegionManager manager = new RegionManager(getApplicationContext(), DatabaseFactory.Get());
            EventBus.getDefault().post(new RegionsSourceEvent(manager.GetRegions()));
        }
        catch (Exception ex){
            Log.d(LogTags.ERROR_PREFIX, "CpiService - GetRegionsEvent" + ex.getMessage());
        }
    }
}
