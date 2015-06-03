package ru.iate.cpi.event;

/**
 * Created by sanea on 03.06.15.
 */
public class DeleteProductEvent {
    public final int ProductId;

    public DeleteProductEvent(int productId){
        this.ProductId = productId;
    }
}
