package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Store;

/**
 * Created by vera on 20.05.15.
 */
public class AddStoreEvent {
    public final Store newStore;

    public AddStoreEvent(Store store){
        newStore = store;
    }
}
