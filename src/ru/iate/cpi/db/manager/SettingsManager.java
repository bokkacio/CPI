package ru.iate.cpi.db.manager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.Settings;
import java.util.List;

/**
 * Created by sanea on 17.05.15.
 */
public class SettingsManager {
    private final OrmLiteDatabaseHelper _db;

    public SettingsManager(OrmLiteDatabaseHelper db){
        _db = db;
    }

    public Settings GetSettingsInfo() throws Exception{
        try {
            Dao<Settings,Integer> daoSetting = _db.getSettingsDao();
            List<Settings> currentSettings = daoSetting.queryForAll();
            return currentSettings.isEmpty() ? null : currentSettings.get(0);
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public void SetSettingsInfo(Settings settings) throws Exception{
        try {
            Dao<Settings,Integer> daoSetting = _db.getSettingsDao();
            //delete all
            DeleteBuilder<Settings, Integer> deleteBuilder = daoSetting.deleteBuilder();
            deleteBuilder.delete();
            //insert new record
            daoSetting.create(settings);
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
