package ru.iate.cpi.db.manager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.Product;

import java.util.List;

/**
 * Created by sanea on 02.06.15.
 */
public class ProductManager {
    private final OrmLiteDatabaseHelper _db;

    public ProductManager(OrmLiteDatabaseHelper db){
        _db = db;
    }

    public List<Product> GetProducts() throws Exception{
        try {
            QueryBuilder<Product,Integer> daoProducts = _db.getProductDao().queryBuilder();
            daoProducts.orderBy(Product.PRODUCT_TITLE_FIELD, true);
            return daoProducts.query();
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public void AddProduct(Product newProduct) throws Exception{
        try {
            Dao<Product,Integer> daoProducts = _db.getProductDao();
            daoProducts.create(newProduct);
        }
        catch (Exception ex){
            throw ex;
        }
    }

    public void DeleteProduct(int productId) throws Exception{
        try {
            Dao<Product,Integer> daoProducts = _db.getProductDao();
            DeleteBuilder<Product, Integer> deleteBuilder = daoProducts.deleteBuilder();
            deleteBuilder.where().eq(Product.PRODUCT_ID_FIELD, productId);
            deleteBuilder.delete();
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
