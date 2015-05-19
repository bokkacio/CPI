package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Store;

import java.util.List;

/**
 * Created by sanea on 14.04.15.
 */
public class StoreEdit extends Activity {
    private List<Store> stores;
    private ListView listViewStoresToEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_edit);

        EventBus.getDefault().register(this);
        initComponents();
    }

    private void initComponents(){
        listViewStoresToEdit = (ListView)findViewById(R.id.listView_storesToEdit);

    }
}