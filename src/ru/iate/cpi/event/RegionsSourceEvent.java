package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Region;
import java.util.List;

/**
 * Created by sanea on 11.05.15.
 */
public class RegionsSourceEvent {
    public final List<Region> Regions;

    public RegionsSourceEvent(List<Region> regions){
        this.Regions = regions;
    }
}
