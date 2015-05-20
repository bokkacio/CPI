package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Store;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.containers.ListViewElement;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanea on 14.04.15.
 */
public class StoreEdit extends Activity {
    private List<Store> stores;

    private ListView listViewStoresToEdit;
    private EditText editTextStoreTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_edit);

        EventBus.getDefault().register(this);
        initComponents();
    }

    private void initComponents(){
        listViewStoresToEdit = (ListView)findViewById(R.id.listView_storesToEdit);
        editTextStoreTitle = (EditText)findViewById(R.id.editText_storeTitle);

        //extract regions
        if(stores == null)
            EventBus.getDefault().post(new GetStoresEvent());
        else
            initListView();
    }

    public void AddStore(View view){
        String storeTitle = editTextStoreTitle.getText().toString();
        if(storeTitle == "")
            return;

        EventBus.getDefault().post(new AddStoreEvent(new Store(storeTitle)));
    }

    public void RemoveStore(View view){
        //EventBus.getDefault().post(new GetStoresEvent());
    }

    //Extract stores from DB
    public void onEventMainThread(StoresSourceEvent event){
        stores = event.Stores;
        initListView();
    }

    private void initListView(){
        ArrayList<ListViewElement> list = new ArrayList<ListViewElement>();
        for (int i = 0; i < this.stores.size(); i++) {
            list.add(new ListViewElement(stores.get(i).GetId(), stores.get(i).GetTitle()));
        }

        ArrayAdapter<ListViewElement> dataAdapter = new ArrayAdapter<ListViewElement>(this,
                android.R.layout.simple_list_item_1, list);

        listViewStoresToEdit.setAdapter(dataAdapter);
    }
}