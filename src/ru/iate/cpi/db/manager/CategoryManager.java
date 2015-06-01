package ru.iate.cpi.db.manager;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import ru.iate.cpi.R;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.Category;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by sanea on 30.05.15.
 */
public class CategoryManager {
    private final Context _context;
    private final OrmLiteDatabaseHelper _db;

    public CategoryManager(Context context, OrmLiteDatabaseHelper db){
        _context = context;
        _db = db;
    }

    public void FillCategories() throws Exception{
        String line;
        Category cpiCategory;
        InputStream sourceStream = _context.getResources().openRawResource(R.raw.cpi_categories);
        BufferedReader r = new BufferedReader(new InputStreamReader(sourceStream));

        try {
            Dao<Category,Integer> daoCategories = _db.getCategoryDao();
            while ((line = r.readLine()) != null) {
                //0 - code
                //1 - title
                //2 - weight
                String[] splittedArray = line.split(";");
                float weight = Float.parseFloat(splittedArray[2]);
                String code = splittedArray[0].trim();
                int codeLevel = code.split("\\.").length;
                cpiCategory = new Category(codeLevel,code,splittedArray[1].trim(),weight);
                daoCategories.create(cpiCategory);
            }
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public List<Category> GetCategories() throws Exception{
        try {
            QueryBuilder<Category,Integer> daoCategories = _db.getCategoryDao().queryBuilder();
            daoCategories.orderBy(Category.CATEGORY_CODE_FIELD, true);
            return daoCategories.query();
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public void UpdateCategory(int categoryId, float weight) throws Exception{
        try {
            UpdateBuilder<Category, Integer> updateBuilder = _db.getCategoryDao().updateBuilder();
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq(Category.CATEGORY_ID_FIELD, categoryId);
            // update the value of your field(s)
            updateBuilder.updateColumnValue(Category.CATEGORY_WEIGHT_FIELD /* column */, weight /* value */);
            updateBuilder.update();
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
