package ru.iate.cpi.event;
import ru.iate.cpi.db.table.Data;

import java.util.Date;

/**
 * Created by sanea on 04.06.15.
 */
public class AddPriceEvent {
//    public final int StoreId;
//    public final int CategoryId;
//    public final int ProductId;
//    public final int  QuantityId;
//    public final int RegionId;
//    public final int Price;
//    public final Date SubmitDate;
//
//    public AddPriceEvent(int storeId,
//                int categoryId,
//                int productId,
//                int quantityId,
//                int regionId,
//                int price,
//                Date submitDate){
//        this.StoreId = storeId;
//        this.CategoryId = categoryId;
//        this.ProductId = productId;
//        this.QuantityId = quantityId;
//        this.RegionId = regionId;
//        this.Price = price;
//        this.SubmitDate = submitDate;
//    }

    public final Data PriceData;

    public AddPriceEvent(Data priceData){
        this.PriceData = priceData;
    }
}
