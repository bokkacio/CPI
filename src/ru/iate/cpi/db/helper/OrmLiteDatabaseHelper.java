package ru.iate.cpi.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.iate.cpi.db.table.*;

import java.sql.SQLException;

/**
 * Created by sanea on 04.05.15.
 */
public class OrmLiteDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "cpi_main.db";
    private static final int DATABASE_VERSION = 3;

    private Dao<Category, Integer> categoryDao = null;
    private Dao<Data, Integer> dataDao = null;
    private Dao<Product, Integer> productDao = null;
    private Dao<Quantity, Integer> quantityDao = null;
    private Dao<Region, Integer> regionDao = null;
    private Dao<Settings, Integer> settingsDao = null;
    private Dao<Store, Integer> storeDao = null;

    public OrmLiteDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try
        {
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Data.class);
            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, Quantity.class);
            TableUtils.createTable(connectionSource, Region.class);
            TableUtils.createTable(connectionSource, Settings.class);
            TableUtils.createTable(connectionSource, Store.class);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer){
        try{
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Data.class);
            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, Quantity.class);
            TableUtils.createTable(connectionSource, Region.class);
            TableUtils.createTable(connectionSource, Settings.class);
            TableUtils.createTable(connectionSource, Store.class);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Returns the Database Access Object (DAO). It will create it or just give the cached
    public Dao<Category, Integer> getCategoryDao() throws SQLException {
        if (categoryDao == null) {
            categoryDao = getDao(Category.class);
        }
        return categoryDao;
    }

    public Dao<Data, Integer> getDataDao() throws SQLException {
        if (dataDao == null) {
            dataDao = getDao(Data.class);
        }
        return dataDao;
    }

    public Dao<Product, Integer> getProductDao() throws SQLException {
        if (productDao == null) {
            productDao = getDao(Product.class);
        }
        return productDao;
    }

    public Dao<Quantity, Integer> getQuantityDao() throws SQLException {
        if (quantityDao == null) {
            quantityDao = getDao(Quantity.class);
        }
        return quantityDao;
    }

    public Dao<Region, Integer> getRegionDao() throws SQLException {
        if (regionDao == null) {
            regionDao = getDao(Region.class);
        }
        return regionDao;
    }

    public Dao<Settings, Integer> getSettingsDao() throws SQLException {
        if (settingsDao == null) {
            settingsDao = getDao(Settings.class);
        }
        return settingsDao;
    }

    public Dao<Store, Integer> getStoreDao() throws SQLException {
        if (storeDao == null) {
            storeDao = getDao(Store.class);
        }
        return storeDao;
    }

    //Close the database connections and clear any cached DAOs.
    @Override
    public void close() {
        super.close();
        categoryDao = null;
        dataDao = null;
        productDao = null;
        quantityDao = null;
        regionDao = null;
        settingsDao = null;
        storeDao = null;
    }
}
