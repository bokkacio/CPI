package ru.iate.cpi.db.table;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by sanea on 04.05.15.
 */

@DatabaseTable(tableName = "cpi_quantity")
public class Quantity {

    public final static String QUANTITY_ID_FIELD = "quantity_id";
    public final static String QUANTITY_TITLE_FIELD = "quantity_title";

    public Quantity(){
        //needed by ormlite
    }

    @DatabaseField(generatedId = true, columnName = QUANTITY_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = QUANTITY_TITLE_FIELD)
    private String Title;
}
