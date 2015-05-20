package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Store;

import java.util.List;

/**
 * Created by vera on 20.05.15.
 */
public class StoresSourceEvent {
    public List<Store> Stores;

    public StoresSourceEvent(List<Store> stores){
        Stores = stores;
    }
}
