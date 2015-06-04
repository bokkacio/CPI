package ru.iate.cpi.db.table;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * Created by sanea on 04.05.15.
 */
@DatabaseTable(tableName = "cpi_data")
public class Data {
    public final static String DATA_ID_FIELD = "data_id";
    public final static String DATA_PRICE_FIELD = "price";
    public final static String DATA_SUBMIT_DATE_FIELD = "submit_date";

    public Data(){
        //needed by ormlite
    }

    public Data(int storeId,
                int categoryId,
                int productId,
                int quantityId,
                int regionId,
                int price,
                Date submitDate){
        this.StoreId = storeId;
        this.CategoryId = categoryId;
        this.ProductId = productId;
        this.QuantityId = quantityId;
        this.RegionId = regionId;
        this.Price = price;
        this.SubmitDate = submitDate;
    }

    @DatabaseField(generatedId = true, columnName = DATA_ID_FIELD)
    private int Id;

    @DatabaseField(canBeNull = false, index = true, dataType = DataType.INTEGER, columnName = Store.STORE_ID_FIELD)
    private int StoreId;

    @DatabaseField(canBeNull = false, index = true, dataType = DataType.INTEGER, columnName = Category.CATEGORY_ID_FIELD)
    private int CategoryId;

    @DatabaseField(canBeNull = false, index = true, dataType = DataType.INTEGER, columnName = Product.PRODUCT_ID_FIELD)
    private int ProductId;

    @DatabaseField(canBeNull = false, index = true, dataType = DataType.INTEGER, columnName = Quantity.QUANTITY_ID_FIELD)
    private int  QuantityId;

    @DatabaseField(canBeNull = false, index = true, dataType = DataType.INTEGER, columnName = Region.REGION_ID_FIELD)
    private int RegionId;

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER, columnName = DATA_PRICE_FIELD)
    private int Price;

    @DatabaseField(canBeNull = false, dataType = DataType.DATE, columnName = DATA_SUBMIT_DATE_FIELD)
    private Date SubmitDate;

    public int GetId(){
        return this.Id;
    }

    public int GetPrice(){
        return this.Price;
    }

    public Date GetSubmitDate(){
        return this.SubmitDate;
    }
}
