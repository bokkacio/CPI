package ru.iate.cpi.db.manager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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

    public void AddStore(Store newStore) throws Exception{
        try {
            Dao<Store,Integer> daoStores = _db.getStoreDao();
            daoStores.create(newStore);
        }
        catch (Exception ex){
            throw ex;
        }
    }

    public void DeleteStore(int storeId) throws Exception{
        try {
            Dao<Store,Integer> daoStores = _db.getStoreDao();
            DeleteBuilder<Store, Integer> deleteBuilder = daoStores.deleteBuilder();
            deleteBuilder.where().eq(Store.STORE_ID_FIELD, storeId);
            deleteBuilder.delete();
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
