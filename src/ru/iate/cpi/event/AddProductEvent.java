package ru.iate.cpi.event;

/**
 * Created by sanea on 03.06.15.
 */
public class AddProductEvent {
    public final String ProductTitle;
    public final int CategoryId;

    public AddProductEvent(String productTitle, int categoryId){
        this.ProductTitle = productTitle;
        this.CategoryId = categoryId;
    }
}
