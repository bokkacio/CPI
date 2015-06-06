package ru.iate.cpi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import de.greenrobot.event.EventBus;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Category;
import ru.iate.cpi.event.CategoriesSourceEvent;
import ru.iate.cpi.event.EditCategoryEvent;
import ru.iate.cpi.event.GetCategoriesEvent;
import ru.iate.cpi.ui.FormatHelper;
import ru.iate.cpi.ui.OptionMenuCodes;
import ru.iate.cpi.ui.adapters.CategoryExpandableAdapter;
import ru.iate.cpi.ui.containers.SpinnerElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sanea on 14.04.15.
 */
public class CategoryEdit extends Activity{
    private Spinner spinnerCategoryGroup;
    private ExpandableListView expandableCategoryItems;
    private EditText editCategoryWeight;

    private InputMethodManager inputManager;

    private CategoryExpandableAdapter expandableAdapter;
    private List<Category> categories;
    private List<Category> groupCategories;
    private List<Category> subGroupCategories;

    private Category selectedGroup, selectedItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_edit);

        expandableAdapter = new CategoryExpandableAdapter(this);
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
                finish();
                break;
            }
            case OptionMenuCodes.EXIT:
            {
                setResult(OptionMenuCodes.EXIT);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Extract categories from DB
    public void onEventMainThread(CategoriesSourceEvent event){
        categories = event.Categories;
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
        expandableCategoryItems.setAdapter(expandableAdapter.getExpandableAdapter(selectedGroup, categories));
        editCategoryWeight.setText("");
        //hide keyboard
        inputManager.hideSoftInputFromWindow(editCategoryWeight.getWindowToken(), 0);
        selectedItem = null;
    }

    public void changeCategoryWeight(View view){
        String newValue = editCategoryWeight.getText().toString();
        float newWeight;
        if(newValue == null || newValue.equals("") || selectedItem == null)
            return;
        try
        {
            newWeight = Float.valueOf(newValue).floatValue();
        }
        catch (NumberFormatException nfe)
        {
            nfe.getMessage();
            Toast.makeText(this, "Неверный формат", Toast.LENGTH_SHORT).show();
            return;
        }
        EventBus.getDefault().post(new EditCategoryEvent(newWeight, selectedItem.GetId()));
        Toast.makeText(this, "Значение сохранено", Toast.LENGTH_SHORT).show();
    }

    private void initComponents(){
        spinnerCategoryGroup = (Spinner)findViewById(R.id.spinner_categoryGroupEdit);
        expandableCategoryItems = (ExpandableListView)findViewById(R.id.expandable_categoryEdit);
        editCategoryWeight = (EditText)findViewById(R.id.editText_categoryWeight);

        inputManager= (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //extract categories
        if(categories == null)
            EventBus.getDefault().post(new GetCategoriesEvent());
        else{
            initSpinner();
            expandableCategoryItems.setAdapter(expandableAdapter.getExpandableAdapter(selectedGroup, categories));
        }

        //TODO: how to set null to selectedItem after deselection
        expandableCategoryItems.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                v.setSelected(true);
                HashMap child = (HashMap) expandableCategoryItems.getExpandableListAdapter().getChild(groupPosition, childPosition);
                String childCode = (String) child.get("itemCode");
                for (Category category : categories)
                    if (childCode.equals(category.GetCode())) {
                        selectedItem = category;
                        editCategoryWeight.setText(FormatHelper.GetFloat(selectedItem.GetWeight()));
                        break;
                    }
                return false;
            }
        });
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

                expandableCategoryItems.setAdapter(expandableAdapter.getExpandableAdapter(selectedGroup, categories));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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