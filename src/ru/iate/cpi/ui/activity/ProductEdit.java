package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Category;
import ru.iate.cpi.db.table.Product;
import ru.iate.cpi.event.*;
import ru.iate.cpi.ui.adapters.CategoryExpandableAdapter;
import ru.iate.cpi.ui.containers.ListViewElement;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sanea on 13.04.15.
 */
public class ProductEdit extends Activity {
    private Spinner spinnerCategoryGroup;
    private ExpandableListView expandableCategoryItems;
    private ListView listProducts;
    private EditText editProductTitle;

    private CategoryExpandableAdapter expandableAdapter;
    private InputMethodManager inputManager;

    private List<Product> products;
    private List<Category> categories;
    private List<Category> groupCategories;
    private List<Category> subGroupCategories;

    private Category selectedGroup, selectedCategory;
    private ListViewElement selectedProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);

        expandableAdapter = new CategoryExpandableAdapter(this);
        EventBus.getDefault().register(this);
        initComponents();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //Extract categories from DB
    public void onEventMainThread(ProductsAndCategoriesSourceEvent event){
        categories = event.categories;
        products = event.products;
        groupCategories = new ArrayList<Category>();
        subGroupCategories = new ArrayList<Category>();

        for (Category category : categories)
            if(category.GetLevel() == Category.LEVEL_GROUP)
                groupCategories.add(category);

        selectedGroup = groupCategories.get(0);
        for (Category category : categories)
            if(category.GetLevel() == Category.LEVEL_SUBGROUP &&
                    category.GetCode().contains(selectedGroup.GetCode()))
                subGroupCategories.add(category);

        initSpinner();
        expandableCategoryItems.setAdapter(expandableAdapter.getExpandableAdapter(subGroupCategories, categories));
        initListView();
        restoreEditText();
        selectedCategory = null;
    }

    private void initComponents() {
        spinnerCategoryGroup = (Spinner) findViewById(R.id.spinner_productCategoryGroup);
        expandableCategoryItems = (ExpandableListView) findViewById(R.id.expandable_productCategory);
        listProducts = (ListView) findViewById(R.id.listView_productsToEdit);
        editProductTitle = (EditText) findViewById(R.id.edit_productTitle);

        inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //extract categories
        if (categories == null)
            EventBus.getDefault().post(new GetProductsAndCategoriesEvent());
        else {
            initSpinner();
            expandableCategoryItems.setAdapter(expandableAdapter.getExpandableAdapter(subGroupCategories, categories));
            initListView();
        }

        //TODO: how to set null to selectedItem after deselection
        expandableCategoryItems.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                v.setSelected(true);
                HashMap child = (HashMap) expandableCategoryItems.getExpandableListAdapter().getChild(groupPosition, childPosition);
                String childCode = (String)child.get(CategoryExpandableAdapter.ITEM_CODE);
                for (Category category : categories)
                    if(childCode.equals(category.GetCode())){
                        selectedCategory = category;
                        break;
                    }

                initListView();
                return false;
            }
        });
    }

    public void AddProduct(View view){
        String productTitle = editProductTitle.getText().toString();
        if(productTitle.equals(""))
            return;

        for (Product product : products)
            if(product.GetTitle().equals(productTitle)){
                restoreEditText();
                Toast.makeText(this, String.format("Продукт %s уже существует", productTitle), Toast.LENGTH_SHORT).show();
                return;
            }

        EventBus.getDefault().post(new AddProductEvent(productTitle, selectedCategory.GetId()));
        restoreEditText();
    }

    public void DeleteProduct(View view){
        if(selectedProduct == null)
            return;

        EventBus.getDefault().post(new DeleteProductEvent(selectedProduct.Id));
    }

    private void restoreEditText(){
        editProductTitle.setText("");
        //hide keyboard
        inputManager.hideSoftInputFromWindow(editProductTitle.getWindowToken(), 0);
    }

    private void initSpinner(){
        List<SpinnerElement> groupList = new ArrayList<SpinnerElement>();

        for (int i = 0; i < groupCategories.size(); i++)
            groupList.add(new SpinnerElement(groupCategories.get(i).GetId(), groupCategories.get(i).GetTextLayout()));

        ArrayAdapter<SpinnerElement> dataAdapter = new ArrayAdapter<SpinnerElement>(this,
                android.R.layout.simple_spinner_item, groupList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryGroup.setAdapter(dataAdapter);
        spinnerCategoryGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                SpinnerElement selected = (SpinnerElement) spinnerCategoryGroup.getItemAtPosition(position);
                subGroupCategories.clear();
                for (Category category : groupCategories)
                    if (category.GetId() == selected.Id) {
                        selectedGroup = category;
                        break;
                    }
                for (int i = 0; i < categories.size(); i++)
                    if (categories.get(i).GetLevel() == Category.LEVEL_SUBGROUP &&
                            categories.get(i).GetCode().contains(selectedGroup.GetCode()))
                        subGroupCategories.add(categories.get(i));

                expandableCategoryItems.setAdapter(expandableAdapter.getExpandableAdapter(subGroupCategories, categories));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initListView(){
        ArrayList<ListViewElement> list = new ArrayList<ListViewElement>();
        selectedProduct = null;

        for (int i = 0; i < this.products.size(); i++)
            if(selectedCategory != null && products.get(i).GetCategoryId() == selectedCategory.GetId())
                list.add(new ListViewElement(products.get(i).GetId(), products.get(i).GetTitle()));

        ArrayAdapter<ListViewElement> dataAdapter = new ArrayAdapter<ListViewElement>(this,
                android.R.layout.simple_list_item_1, list);

        listProducts.setAdapter(dataAdapter);
        listProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                for (int i = 0; i < listProducts.getChildCount(); i++) {
                    View listElement = listProducts.getChildAt(i);
                    listElement.setBackgroundColor(getResources().getColor(R.color.mint));
                }
                selectedProduct = (ListViewElement) listProducts.getItemAtPosition(position);
                view.setBackgroundColor(Color.CYAN);
            }
        });
    }
}