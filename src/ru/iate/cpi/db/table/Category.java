package ru.iate.cpi.db.table;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by sanea on 04.05.15.
 */

@DatabaseTable(tableName = "cpi_category")
public class Category {

    public final static String CATEGORY_ID_FIELD = "category_id";
    public final static String CATEGORY_CODE_FIELD = "category_code";
    public final static String CATEGORY_TITLE_FIELD = "category_title";
    public final static String CATEGORY_WEIGHT_FIELD = "category_weight";

    public Category(){
        //needed by ormlite
    }

    @DatabaseField(generatedId = true, columnName = CATEGORY_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = CATEGORY_CODE_FIELD)
    private String Code;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = CATEGORY_TITLE_FIELD)
    private String Title;

    @DatabaseField(canBeNull = false, dataType = DataType.FLOAT, columnName = CATEGORY_WEIGHT_FIELD)
    private float Weight;
}
