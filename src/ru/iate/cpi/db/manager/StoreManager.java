package ru.iate.cpi.db.manager;

import com.j256.ormlite.stmt.QueryBuilder;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.Store;

import java.util.List;

/**
 * Created by sanea on 19.05.15.
 */
public class StoreManager {
    private final OrmLiteDatabaseHelper _db;

    public StoreManager(OrmLiteDatabaseHelper db){
        _db = db;
    }

    public List<Store> GetStores() throws Exception{
        try {
            QueryBuilder<Store,Integer> daoStores = _db.getStoreDao().queryBuilder();
            daoStores.orderBy(Store.STORE_TITLE_FIELD, true);
            return daoStores.query();
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
