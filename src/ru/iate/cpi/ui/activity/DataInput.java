package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.*;
import ru.iate.cpi.event.*;
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
        switch (view.getId())
        {
            case R.id.spinner_dataStore:
                Toast.makeText(this, "spinner_dataStore", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_dataCategoryGroup:
                Toast.makeText(this, "spinner_dataCategoryGroup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_dataCategorySubGroup:
                Toast.makeText(this, "spinner_dataCategorySubGroup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_dataCategory:
                Toast.makeText(this, "spinner_dataCategory", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_dataProduct:
                Toast.makeText(this, "spinner_dataProduct", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_dataQuantity:
                Toast.makeText(this, "spinner_dataQuantity", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    //Extract prices and requisites from DB
    public void onEventMainThread(PricesAndRequisitesSourceEvent event){
        stores = event.Stores;
        categories = event.Categories;
        products = event.Products;
        quantities = event.Quantities;
        currentSettings = event.CurrentSettings;
        priceData = event.PriceData;

        initQuantitySpinner();
        initSroreSpinner();
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

    private void initSroreSpinner(){
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

        spinnerStore.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private ArrayAdapter<SpinnerElement> getSpinnerAdapter(List<SpinnerElement> elements){
        ArrayAdapter<SpinnerElement> dataAdapter = new ArrayAdapter<SpinnerElement>(this,
                android.R.layout.simple_spinner_item, elements);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }
}