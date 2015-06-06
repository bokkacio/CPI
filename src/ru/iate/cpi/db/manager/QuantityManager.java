package ru.iate.cpi.db.manager;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import ru.iate.cpi.R;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.Quantity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by sanea on 03.06.15.
 */
public class QuantityManager {
    private final Context _context;
    private final OrmLiteDatabaseHelper _db;

    public QuantityManager(Context context, OrmLiteDatabaseHelper db){
        this._context = context;
        this._db = db;
    }

    public void FillQuantities() throws Exception{
        String line;
        Quantity quantity;
        InputStream sourceStream = _context.getResources().openRawResource(R.raw.cpi_amount_types);
        BufferedReader r = new BufferedReader(new InputStreamReader(sourceStream));

        try {
            Dao<Quantity,Integer> daoQuantities = _db.getQuantityDao();
            while ((line = r.readLine()) != null) {
                quantity = new Quantity(line.trim());
                daoQuantities.create(quantity);
            }
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public List<Quantity> GetQuantities() throws Exception{
        try {
            QueryBuilder<Quantity,Integer> daoQuantities = _db.getQuantityDao().queryBuilder();
            daoQuantities.orderBy(Quantity.QUANTITY_ID_FIELD, true);
            return daoQuantities.query();
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
