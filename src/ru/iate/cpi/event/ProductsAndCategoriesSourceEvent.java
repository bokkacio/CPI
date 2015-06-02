package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Category;
import ru.iate.cpi.db.table.Product;

import java.util.List;

/**
 * Created by sanea on 02.06.15.
 */
public class ProductsAndCategoriesSourceEvent {
    public final List<Product> products;
    public final List<Category> categories;

    public ProductsAndCategoriesSourceEvent(List<Product> products, List<Category> categories){
        this.products = products;
        this.categories = categories;
    }
}
