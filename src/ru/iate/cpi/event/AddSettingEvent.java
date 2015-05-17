package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Settings;

/**
 * Created by sanea on 17.05.15.
 */
public class AddSettingEvent {
    public final Settings settings;

    public AddSettingEvent(Settings settings){
        this.settings = settings;
    }
}
