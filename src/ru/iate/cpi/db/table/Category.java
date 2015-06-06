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
    public final static String CATEGORY_LEVEL_FIELD = "category_level";
    public final static String CATEGORY_CODE_FIELD = "category_code";
    public final static String CATEGORY_TITLE_FIELD = "category_title";
    public final static String CATEGORY_WEIGHT_FIELD = "category_weight";

    public final static int LEVEL_GROUP = 1;
    public final static int LEVEL_SUBGROUP = 3;
    public final static int LEVEL_ITEM = 6;

    public Category(){
        //needed by ormlite
    }

    public Category(int level, String code, String title, float weight){
        this.Level = level;
        this.Code = code;
        this.Title = title;
        this.Weight = weight;
    }

    @DatabaseField(generatedId = true, columnName = CATEGORY_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER, columnName = CATEGORY_LEVEL_FIELD)
    private int Level;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = CATEGORY_CODE_FIELD)
    private String Code;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = CATEGORY_TITLE_FIELD)
    private String Title;

    @DatabaseField(canBeNull = false, dataType = DataType.FLOAT, columnName = CATEGORY_WEIGHT_FIELD)
    private float Weight;

    public int GetId(){
        return this.Id;
    }

    public int GetLevel(){
        return this.Level;
    }

    public String GetCode(){
        return this.Code;
    }

    public String GetTitle(){
        return this.Title;
    }

    public float GetWeight(){
        return this.Weight;
    }

    public String GetTextLayout(){
        return String.format("%s %s", GetCode(), GetTitle());
    }
}
