package ru.iate.cpi.db.table;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by vera on 29.04.15.
 */
@DatabaseTable(tableName = "cpi_region")
public class Region {

    public final static String REGION_ID_FIELD = "region_id";
    public final static String REGION_CODE_FIELD = "region_code";
    public final static String REGION_TITLE_FIELD = "region_title";

    public Region(){
        //needed by ormlite
    }

    public Region(String code, String title) {
        this.Code = code;
        this.Title = title;
    }

    @DatabaseField(generatedId = true, columnName = REGION_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = REGION_CODE_FIELD)
    private String Code;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = REGION_TITLE_FIELD)
    private String Title;

    public int GetId(){
        return this.Id;
    }

    public String GetCode(){
        return this.Code;
    }

    public String GetTitle(){
        return this.Title;
    }
}
