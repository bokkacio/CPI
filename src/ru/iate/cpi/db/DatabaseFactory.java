package ru.iate.cpi.db;

import android.content.Context;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;

/**
 * Created by sanea on 10.05.15.
 */
public class DatabaseFactory {
    private static OrmLiteDatabaseHelper databaseHelper;

    public static OrmLiteDatabaseHelper Get(){
        return databaseHelper;
    }

    public static void Set(Context context){
        databaseHelper = new OrmLiteDatabaseHelper(context);
    }

    public static void Release(){
        databaseHelper.close();
        databaseHelper = null;
    }
}
