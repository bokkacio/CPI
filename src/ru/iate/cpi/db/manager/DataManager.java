package ru.iate.cpi.db.manager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.*;

import java.util.Date;
import java.util.List;

/**
 * Created by sanea on 04.06.15.
 */
public class DataManager {
    private final OrmLiteDatabaseHelper _db;

    public DataManager(OrmLiteDatabaseHelper db){
        _db = db;
    }

    public List<Data> GetData(Date submitDate, int regionId) throws Exception{
        try {
            Dao<Data,Integer> dao = _db.getDataDao();
            QueryBuilder<Data,Integer> daoData = dao.queryBuilder();
            daoData.where().eq(Region.REGION_ID_FIELD, regionId);
            daoData.where().eq(Data.DATA_SUBMIT_DATE_FIELD, submitDate);
            return daoData.query();
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public void AddData(Data priceData) throws Exception{
        try {
            Dao<Data,Integer> dao = _db.getDataDao();
            dao.create(priceData);
        }
        catch (Exception ex){
            throw ex;
        }
    }

    public void DeleteData(int dataId) throws Exception{
        try {
            Dao<Data,Integer> dao = _db.getDataDao();
            DeleteBuilder<Data, Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(Data.DATA_ID_FIELD, dataId);
            deleteBuilder.delete();
        }
        catch (Exception ex){
            throw ex;
        }
    }


}
