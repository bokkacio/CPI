package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Store;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.OptionMenuCodes;
import ru.iate.cpi.ui.containers.ListViewElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanea on 14.04.15.
 */
public class StoreEdit extends Activity {
    private List<Store> stores;

    private ListView listViewStoresToEdit;
    private EditText editTextStoreTitle;
    private InputMethodManager inputManager;
    private ListViewElement selectedStore = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_edit);

        EventBus.getDefault().register(this);
        initComponents();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OptionMenuCodes.DATA_INPUT_ACTIVITY, 0, OptionMenuCodes.DATA_INPUT_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.EXIT, 1, OptionMenuCodes.EXIT_STR);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case OptionMenuCodes.DATA_INPUT_ACTIVITY:
            {
                //start DataInput activity
                Intent intent = new Intent(this, DataInput.class);
                startActivityForResult(intent, OptionMenuCodes.DATA_INPUT_ACTIVITY);
                break;
            }
            case OptionMenuCodes.EXIT:
            {
                setResult(OptionMenuCodes.EXIT);
                break;
            }
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void initComponents(){
        listViewStoresToEdit = (ListView)findViewById(R.id.listView_storesToEdit);
        editTextStoreTitle = (EditText)findViewById(R.id.editText_storeTitle);

        inputManager= (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //extract regions
        if(stores == null)
            EventBus.getDefault().post(new GetStoresEvent());
        else
            initListView();
    }

    public void AddStore(View view){
        String storeTitle = editTextStoreTitle.getText().toString();
        if(storeTitle.equals(""))
            return;

        for (Store store : stores)
            if(store.GetTitle().equals(storeTitle)){
                restoreEditText();
                Toast.makeText(this, String.format("Магазин %s уже существует", storeTitle), Toast.LENGTH_SHORT).show();
                return;
            }

        EventBus.getDefault().post(new AddStoreEvent(new Store(storeTitle)));
        restoreEditText();
    }

    public void RemoveStore(View view){
        if(selectedStore != null)
            EventBus.getDefault().post(new DeleteStoreEvent(selectedStore.Id));

        restoreEditText();
    }

    //Extract stores from DB
    public void onEventMainThread(StoresSourceEvent event){
        stores = event.Stores;
        initListView();
    }

    private void restoreEditText(){
        editTextStoreTitle.setText("");
        listViewStoresToEdit.setFocusable(true);
        //hide keyboard
        inputManager.hideSoftInputFromWindow(editTextStoreTitle.getWindowToken(), 0);
    }

    private void initListView(){
        ArrayList<ListViewElement> list = new ArrayList<ListViewElement>();
        for (int i = 0; i < this.stores.size(); i++) {
            list.add(new ListViewElement(stores.get(i).GetId(), stores.get(i).GetTitle()));
        }

        ArrayAdapter<ListViewElement> dataAdapter = new ArrayAdapter<ListViewElement>(this,
                android.R.layout.simple_list_item_1, list);

        listViewStoresToEdit.setAdapter(dataAdapter);
        listViewStoresToEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                for (int i = 0; i < listViewStoresToEdit.getChildCount(); i++) {
                    View listElement = listViewStoresToEdit.getChildAt(i);
                    listElement.setBackgroundColor(getResources().getColor(R.color.mint));
                }
                selectedStore = (ListViewElement) listViewStoresToEdit.getItemAtPosition(position);
                view.setBackgroundColor(Color.CYAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == OptionMenuCodes.EXIT)
        {
            setResult(OptionMenuCodes.EXIT);
            finish();
        }
    }
}