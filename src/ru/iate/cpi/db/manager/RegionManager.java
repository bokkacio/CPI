package ru.iate.cpi.db.manager;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import ru.iate.cpi.R;
import ru.iate.cpi.db.helper.OrmLiteDatabaseHelper;
import ru.iate.cpi.db.table.Region;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by sanea on 10.05.15.
 */
public class RegionManager {
    private final Context _context;
    private final OrmLiteDatabaseHelper _db;

    public RegionManager(Context context, OrmLiteDatabaseHelper db){
        _context = context;
        _db = db;
    }

    public void FillRegions() throws Exception{
        String line;
        Region cpiRegion;
        InputStream sourceStream = _context.getResources().openRawResource(R.raw.cpi_regions);
        BufferedReader r = new BufferedReader(new InputStreamReader(sourceStream));

        try {
            Dao<Region,Integer> daoRegions = _db.getRegionDao();
            while ((line = r.readLine()) != null) {
                String[] splittedArray = line.split(";");
                cpiRegion = new Region(splittedArray[1].trim(), splittedArray[0].trim());
                daoRegions.create(cpiRegion);
            }
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public List<Region> GetRegions() throws Exception{
        try {
            QueryBuilder<Region,Integer> daoRegions = _db.getRegionDao().queryBuilder();
            daoRegions.orderBy(Region.REGION_CODE_FIELD, true);
            return daoRegions.query();
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public Region GetRegion(int regionId) throws Exception{
        try {
            QueryBuilder<Region,Integer> daoRegions = _db.getRegionDao().queryBuilder();
            Where<Region,Integer> whereDao = daoRegions.where();
            whereDao.in(Region.REGION_ID_FIELD, regionId);
            List<Region> regions = whereDao.query();
            return regions.isEmpty() ? null : regions.get(0);
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
