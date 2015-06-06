package ru.iate.cpi.db.table;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by sanea on 04.05.15.
 */

@DatabaseTable(tableName = "cpi_product")
public class Product {

    public final static String PRODUCT_ID_FIELD = "product_id";
    public final static String PRODUCT_TITLE_FIELD = "product_title";

    public Product(){
        //needed by ormlite
    }

    public Product(String title, int categoryId){
        this.Title = title;
        this.CategoryId = categoryId;
    }

    @DatabaseField(generatedId = true, columnName = PRODUCT_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, index = true, dataType = DataType.INTEGER, columnName = Category.CATEGORY_ID_FIELD)
    private int CategoryId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = PRODUCT_TITLE_FIELD)
    private String Title;

    public int GetId(){
        return this.Id;
    }

    public int GetCategoryId(){
        return this.CategoryId;
    }

    public String GetTitle(){
        return this.Title;
    }
}
