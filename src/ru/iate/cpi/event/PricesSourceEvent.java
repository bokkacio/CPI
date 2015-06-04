package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Data;

import java.util.List;

/**
 * Created by sanea on 04.06.15.
 */
public class PricesSourceEvent {
    public final List<Data> PriceData;

    public PricesSourceEvent(List<Data> priceData){
        this.PriceData = priceData;
    }
}
