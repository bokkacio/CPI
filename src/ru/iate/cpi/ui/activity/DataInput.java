package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.*;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.OptionMenuCodes;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanea on 30.03.15.
 */
public class DataInput extends Activity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerStore;
    private Spinner spinnerCategoryGroup;
    private Spinner spinnerCategorySubGroup;
    private Spinner spinnerCategory;
    private Spinner spinnerProduct;
    private Spinner spinnerQuantity;
    private EditText editPriceData;

    private List<Store> stores;
    private List<Category> categories;
    private List<Product> products;
    private List<Quantity> quantities;
    private ru.iate.cpi.db.table.Settings currentSettings;
    private List<Data> priceData;

    private SpinnerElement quantity, product, category, subGroupCategory, groupCategory, store;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_input);

        EventBus.getDefault().register(this);
        initComponents();
        EventBus.getDefault().post(new GetPricesAndRequisitesEvent());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        switch (((View)view.getParent()).getId())
        {
            case R.id.spinner_dataStore:
                break;
            case R.id.spinner_dataCategoryGroup:
                break;
            case R.id.spinner_dataCategorySubGroup:
                break;
            case R.id.spinner_dataCategory:
                break;
            case R.id.spinner_dataProduct:
                break;
            case R.id.spinner_dataQuantity:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OptionMenuCodes.SETTINGS_ACTIVITY, 0, OptionMenuCodes.SETTINGS_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.STORE_ACTIVITY, 1, OptionMenuCodes.STORE_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.PRODUCT_ACTIVITY, 2, OptionMenuCodes.PRODUCT_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.CATEGORY_ACTIVITY, 3, OptionMenuCodes.CATEGORY_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.EXIT, 4, OptionMenuCodes.EXIT_STR);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case OptionMenuCodes.SETTINGS_ACTIVITY:
            {
                //start DataInput activity
                Intent intent = new Intent(this, Settings.class);
                startActivityForResult(intent, OptionMenuCodes.SETTINGS_ACTIVITY);
                break;
            }
            case OptionMenuCodes.STORE_ACTIVITY:
            {
                //start Store activity
                Intent intent = new Intent(this, StoreEdit.class);
                startActivityForResult(intent, OptionMenuCodes.STORE_ACTIVITY);
                break;
            }
            case OptionMenuCodes.PRODUCT_ACTIVITY:
            {
                //start Product activity
                Intent intent = new Intent(this, ProductEdit.class);
                startActivityForResult(intent, OptionMenuCodes.PRODUCT_ACTIVITY);
                break;
            }
            case OptionMenuCodes.CATEGORY_ACTIVITY:
            {
                //start Category activity
                Intent intent = new Intent(this, CategoryEdit.class);
                startActivityForResult(intent, OptionMenuCodes.CATEGORY_ACTIVITY);
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

    //Extract prices and requisites from DB
    public void onEventMainThread(PricesAndRequisitesSourceEvent event){
        stores = event.Stores;
        categories = event.Categories;
        products = event.Products;
        quantities = event.Quantities;
        currentSettings = event.CurrentSettings;
        priceData = event.PriceData;

        initQuantitySpinner();
        initStoreSpinner();
        initGroupCategorySpinner();
    }

    //Extract prices from DB
    public void onEventMainThread(PricesSourceEvent event){
        priceData = event.PriceData;
    }

    private void initComponents(){
        spinnerStore = (Spinner)findViewById(R.id.spinner_dataStore);
        spinnerCategoryGroup = (Spinner)findViewById(R.id.spinner_dataCategoryGroup);
        spinnerCategorySubGroup = (Spinner)findViewById(R.id.spinner_dataCategorySubGroup);
        spinnerCategory = (Spinner)findViewById(R.id.spinner_dataCategory);
        spinnerProduct = (Spinner)findViewById(R.id.spinner_dataProduct);
        spinnerQuantity = (Spinner)findViewById(R.id.spinner_dataQuantity);
        editPriceData = (EditText)findViewById(R.id.edit_dataPrice);

        spinnerStore.setOnItemSelectedListener(this);
        spinnerCategoryGroup.setOnItemSelectedListener(this);
        spinnerCategorySubGroup.setOnItemSelectedListener(this);
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerProduct.setOnItemSelectedListener(this);
        spinnerQuantity.setOnItemSelectedListener(this);
    }

    private void initQuantitySpinner(){
        List<SpinnerElement> spinnerElements = new ArrayList<SpinnerElement>();
        for (int i = 0; i < quantities.size(); i++)
            spinnerElements.add(new SpinnerElement(quantities.get(i).GetId(), quantities.get(i).GetTitle()));
        spinnerQuantity.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private void initStoreSpinner(){
        List<SpinnerElement> spinnerElements = new ArrayList<SpinnerElement>();
        for (int i = 0; i < stores.size(); i++)
            spinnerElements.add(new SpinnerElement(stores.get(i).GetId(), stores.get(i).GetTitle()));
        spinnerStore.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private void initGroupCategorySpinner(){
        List<SpinnerElement> spinnerElements = new ArrayList<SpinnerElement>();
        for (int i = 0; i < categories.size(); i++)
            if(categories.get(i).GetLevel() == Category.LEVEL_GROUP)
                spinnerElements.add(new SpinnerElement(categories.get(i).GetId(), categories.get(i).GetTitle()));

        spinnerCategoryGroup.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private ArrayAdapter<SpinnerElement> getSpinnerAdapter(List<SpinnerElement> elements){
        ArrayAdapter<SpinnerElement> dataAdapter = new ArrayAdapter<SpinnerElement>(this,
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
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