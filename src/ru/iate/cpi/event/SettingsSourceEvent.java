package ru.iate.cpi.event;
import ru.iate.cpi.db.table.Region;
import ru.iate.cpi.db.table.Settings;

/**
 * Created by sanea on 17.05.15.
 */
public class SettingsSourceEvent {
    public final Settings settings;
    public final Region region;

    public SettingsSourceEvent(Settings settings, Region region){
        this.settings = settings;
        this.region = region;
    }
}
