package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.*;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.LogTags;
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
    private InputMethodManager inputManager;

    private List<Store> stores;
    private List<Category> categories;
    private List<Product> products;
    private List<Quantity> quantities;
    private ru.iate.cpi.db.table.Settings currentSettings;
    private List<Data> priceData;
    private Data currentPrice;

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
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        if(view == null || view.getParent() == null)
            return;

        switch (((View)view.getParent()).getId())
        {
            case R.id.spinner_dataStore:
                store = (SpinnerElement) spinnerStore.getItemAtPosition(position);
                setCurrentPrice();
                break;
            case R.id.spinner_dataCategoryGroup:{
                groupCategory = (SpinnerElement) spinnerCategoryGroup.getItemAtPosition(position);
                initSubGroupCategorySpinner();
                initCategorySpinner();
                initProductSpinner();
                break;
            }
            case R.id.spinner_dataCategorySubGroup:{
                subGroupCategory = (SpinnerElement) spinnerCategorySubGroup.getItemAtPosition(position);
                Log.d(LogTags.ERROR_PREFIX, "onItemSelected - spinner_dataCategorySubGroup " + subGroupCategory.Text);
                initCategorySpinner();
                initProductSpinner();
                break;
            }
            case R.id.spinner_dataCategory:{
                category = (SpinnerElement) spinnerCategory.getItemAtPosition(position);
                initProductSpinner();
                break;
            }
            case R.id.spinner_dataProduct:
                product = (SpinnerElement) spinnerProduct.getItemAtPosition(position);
                setCurrentPrice();
                break;
            case R.id.spinner_dataQuantity:
                quantity = (SpinnerElement) spinnerQuantity.getItemAtPosition(position);
                setCurrentPrice();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OptionMenuCodes.PRODUCT_ACTIVITY, 0, OptionMenuCodes.PRODUCT_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.STORE_ACTIVITY, 1, OptionMenuCodes.STORE_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.SETTINGS_ACTIVITY, 2, OptionMenuCodes.SETTINGS_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.CATEGORY_ACTIVITY, 4, OptionMenuCodes.CATEGORY_ACTIVITY_STR);
        menu.add(0, OptionMenuCodes.EXIT, 5, OptionMenuCodes.EXIT_STR);
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
        initSubGroupCategorySpinner();
        initCategorySpinner();
        initProductSpinner();

        inputManager= (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //Extract prices from DB
    public void onEventMainThread(PricesSourceEvent event){
        priceData = event.PriceData;
        setCurrentPrice();
    }

    public void onSaveDataClick(View view){
        if(category == null || store == null ||
            quantity == null || product == null ||
            editPriceData.getText().toString().equals(""))
            return;

        int price = Integer.parseInt(editPriceData.getText().toString());
        boolean isNewPrice = currentPrice == null;
        Data priceData = isNewPrice ?
                new Data(store.Id, product.Id, quantity.Id, currentSettings.GetRegionId(), price, currentSettings.GetWorkingPeriod()) :
                new Data(currentPrice.GetId(), price);

        //hide keyboard
        inputManager.hideSoftInputFromWindow(editPriceData.getWindowToken(), 0);
        EventBus.getDefault().post(new AddPriceEvent(priceData, isNewPrice));
        Toast.makeText(this, "Значение цены сохранено", Toast.LENGTH_SHORT).show();
    }

    public void onDeleteDataClick(View view){
        if(currentPrice == null)
            return;

        editPriceData.setText("");
        //hide keyboard
        inputManager.hideSoftInputFromWindow(editPriceData.getWindowToken(), 0);
        EventBus.getDefault().post(new DeletePriceEvent(currentPrice.GetId()));
        Toast.makeText(this, "Значение цены удалено", Toast.LENGTH_SHORT).show();
    }

    private void setCurrentPrice(){
        editPriceData.setText("");
        if(product == null || quantity == null || store == null)
            return;

        currentPrice = null;
        for(Data data : priceData)
            if(data.GetProductId() == product.Id &&
                data.GetQuantityId() == quantity.Id &&
                data.GetStoreId() == store.Id)
            {
                editPriceData.setText("" + data.GetPrice());
                currentPrice = data;
                break;
            }
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

        groupCategory = spinnerElements.isEmpty() ? null : spinnerElements.get(0);
        spinnerCategoryGroup.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private void initSubGroupCategorySpinner(){
        List<SpinnerElement> spinnerElements = new ArrayList<SpinnerElement>();
        Category groupItem = null;

        if(groupCategory == null)
            return;

        for (int i = 0; i < categories.size(); i++)
            if(categories.get(i).GetId() == groupCategory.Id){
                groupItem = categories.get(i);
                break;
            }

        for (int i = 0; i < categories.size(); i++)
            if(categories.get(i).GetLevel() == Category.LEVEL_SUBGROUP &&
                categories.get(i).GetCode().contains(groupItem.GetCode()))
                spinnerElements.add(new SpinnerElement(categories.get(i).GetId(), categories.get(i).GetTitle()));

        subGroupCategory = spinnerElements.isEmpty() ? null : spinnerElements.get(0);
        spinnerCategorySubGroup.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private void initCategorySpinner(){
        List<SpinnerElement> spinnerElements = new ArrayList<SpinnerElement>();
        Category subGroupItem = null;

        if(subGroupCategory == null)
            return;

        for (int i = 0; i < categories.size(); i++)
            if(categories.get(i).GetId() == subGroupCategory.Id){
                subGroupItem = categories.get(i);
                break;
            }

        for (int i = 0; i < categories.size(); i++)
            if(categories.get(i).GetLevel() == Category.LEVEL_ITEM &&
                    categories.get(i).GetCode().contains(subGroupItem.GetCode()))
                spinnerElements.add(new SpinnerElement(categories.get(i).GetId(), categories.get(i).GetTitle()));

        category = spinnerElements.isEmpty() ? null : spinnerElements.get(0);
        spinnerCategory.setAdapter(getSpinnerAdapter(spinnerElements));
    }

    private void initProductSpinner(){
        List<SpinnerElement> spinnerElements = new ArrayList<SpinnerElement>();
        if(category == null)
            return;

        for (int i = 0; i < products.size(); i++)
            if(products.get(i).GetCategoryId() == category.Id)
                spinnerElements.add(new SpinnerElement(products.get(i).GetId(), products.get(i).GetTitle()));

        product = spinnerElements.isEmpty() ? null : spinnerElements.get(0);
        spinnerProduct.setAdapter(getSpinnerAdapter(spinnerElements));
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