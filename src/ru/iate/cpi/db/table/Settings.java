package ru.iate.cpi.db.table;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * Created by sanea on 04.05.15.
 */

@DatabaseTable(tableName = "cpi_settings")
public class Settings {
    public final static String SETTINGS_ID_FIELD = "settings_id";
    public final static String SETTINGS_WORKING_PERIOD_FIELD = "working_period";

    public Settings(){
        //needed by ormlite
    }

    public Settings(int regionId, Date workingPeriod){
        this.RegionId = regionId;
        this.WorkingPeriod = workingPeriod;
    }

    @DatabaseField(generatedId = true, columnName = SETTINGS_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER, columnName = Region.REGION_ID_FIELD)
    private int RegionId;

    @DatabaseField(canBeNull = false, dataType = DataType.DATE, columnName = SETTINGS_WORKING_PERIOD_FIELD)
    private Date WorkingPeriod;

    public int GetRegionId(){
        return RegionId;
    }

    public Date GetWorkingPeriod(){
        return WorkingPeriod;
    }
}
