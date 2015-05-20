package ru.iate.cpi.event;

/**
 * Created by vera on 20.05.15.
 */
public class DeleteStoreEvent {
    public final int storeId;

    public DeleteStoreEvent(int storeId){
        this.storeId = storeId;
    }
}
