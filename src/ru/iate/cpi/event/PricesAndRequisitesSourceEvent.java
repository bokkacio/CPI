package ru.iate.cpi.event;

import ru.iate.cpi.db.table.*;

import java.util.List;

/**
 * Created by sanea on 04.06.15.
 */
public class PricesAndRequisitesSourceEvent {
    public final List<Category> Categories;
    public final List<Product> Products;
    public final List<Store> Stores;
    public final List<Quantity> Quantities;
    public final List<Data> PriceData;
    public final Settings CurrentSettings;

    public PricesAndRequisitesSourceEvent(List<Category> categories,
                                          List<Product> products,
                                          List<Store> stores,
                                          List<Quantity> quantities,
                                          List<Data> priceData,
                                          Settings currentSettings){
        this.Categories = categories;
        this.Products = products;
        this.Stores = stores;
        this.Quantities = quantities;
        this.PriceData = priceData;
        this.CurrentSettings = currentSettings;
    }
}
