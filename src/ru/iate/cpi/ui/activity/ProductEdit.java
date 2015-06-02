package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;

/**
 * Created by sanea on 13.04.15.
 */
public class ProductEdit extends Activity {
    private Spinner spinnerCategoryGroup;
    private ExpandableListView expandableCategoryItems;
    private ListView listProducts;
    private EditText editProductTitle;

    private InputMethodManager inputManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);

        EventBus.getDefault().register(this);
        initComponents();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initComponents(){
        spinnerCategoryGroup = (Spinner)findViewById(R.id.spinner_productCategoryGroup);
        expandableCategoryItems = (ExpandableListView)findViewById(R.id.expandable_productCategory);
        listProducts = (ListView)findViewById(R.id.listView_productsToEdit);
        editProductTitle = (EditText)findViewById(R.id.edit_productTitle);


    }
}