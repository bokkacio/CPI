package ru.iate.cpi.ui.adapters;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;
import ru.iate.cpi.R;
import ru.iate.cpi.db.table.Category;
import ru.iate.cpi.ui.FormatHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sanea on 02.06.15.
 */
public class CategoryExpandableAdapter {
    private final Context _context;
    private List<Category> _subGroupCategories, _categories;

    public static final String SUB_GROUP_CODE = "subGroupCode";
    public static final String SUB_GROUP_TITLE = "subGroupTitle";
    public static final String ITEM_CODE = "itemCode";
    public static final String ITEM_CODE_WEIGHT = "itemCodeWeight";
    public static final String ITEM_TITLE = "itemTitle";

    public CategoryExpandableAdapter(Context context){
        _context = context;
    }

    /*create the HashMap for the group  */
    @SuppressWarnings("unchecked")
    private List getGroupList() {
        ArrayList result = new ArrayList();
        for(int i = 0 ; i < _subGroupCategories.size() ; ++i ) {
            HashMap<String, Object> group = new HashMap<String, Object>();
            group.put(SUB_GROUP_CODE, _subGroupCategories.get(i).GetCode());
            group.put(SUB_GROUP_TITLE, _subGroupCategories.get(i).GetTitle());
            result.add(group);
        }
        return (List)result;
    }

    /* create the HashMap for the child */
    @SuppressWarnings("unchecked")
    private List getChildList() {
        ArrayList result = new ArrayList();
        for( int i = 0 ; i < _subGroupCategories.size() ; ++i ) {
            ArrayList childList = new ArrayList();
            for( int j = 0 ; j < _categories.size() ; j++ ) {
                if(!_categories.get(j).GetCode().contains(_subGroupCategories.get(i).GetCode()))
                    continue;
                HashMap child = new HashMap();
                child.put(ITEM_CODE, _categories.get(j).GetCode());
                child.put(ITEM_CODE_WEIGHT, String.format("%s - %S", _categories.get(j).GetCode(), FormatHelper.GetFloat(_categories.get(j).GetWeight())));
                child.put(ITEM_TITLE, _categories.get(j).GetTitle());
                childList.add( child );
            }
            result.add(childList);
        }
        return result;
    }

    public SimpleExpandableListAdapter getExpandableAdapter(List<Category> subGroupCategories, List<Category> categories){
        _categories = categories;
        _subGroupCategories = subGroupCategories;

        SimpleExpandableListAdapter expListAdapter =
                new SimpleExpandableListAdapter(
                        _context,
                        getGroupList(),              // Creating group List.
                        R.layout.category_group_item,             // Group item layout XML.
                        new String[] { SUB_GROUP_CODE, SUB_GROUP_TITLE },  // the key of group item.
                        // ID of each group item.-Data under the key goes into this TextView.
                        new int[] { R.id.expandable_categorySubGroupCode, R.id.expandable_categorySubGroupTitle },
                        getChildList(),              // childData describes second-level entries.
                        R.layout.category_child_item,             // Layout for sub-level entries(second level).
                        new String[] {ITEM_CODE_WEIGHT, ITEM_TITLE},      // Keys in childData maps to display.
                        // Data under the keys above go into these TextViews.
                        new int[] { R.id.expandable_categoryItemCode, R.id.expandable_categoryItemTitle}
                );
        return expListAdapter;
    }
}
