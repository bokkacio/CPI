package ru.iate.cpi.calc;

import android.content.Context;
import android.util.Log;
import ru.iate.cpi.db.DatabaseFactory;
import ru.iate.cpi.db.manager.CategoryManager;
import ru.iate.cpi.db.manager.DataManager;
import ru.iate.cpi.db.manager.ProductManager;
import ru.iate.cpi.db.manager.SettingsManager;
import ru.iate.cpi.db.table.Category;
import ru.iate.cpi.db.table.Data;
import ru.iate.cpi.db.table.Product;
import ru.iate.cpi.db.table.Settings;
import ru.iate.cpi.ui.LogTags;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sanea on 09.06.15.
 */
public class CpiCalculation {

    private final DataManager _dataManager;
    private final SettingsManager _settingsManager;
    private final CategoryManager _categoryManager;
    private final ProductManager _productManager;

    private Settings currentSettings;
    private List<Data> currentMonthDataSource;
    private List<Data> previousMonthDataSource;
    private List<Category> categorySource;
    private List<Product> productSource;

    public CpiCalculation(Context context){
        _dataManager = new DataManager(DatabaseFactory.Get());
        _settingsManager = new SettingsManager(DatabaseFactory.Get());
        _categoryManager = new CategoryManager(context, DatabaseFactory.Get());
        _productManager = new ProductManager(DatabaseFactory.Get());
    }

    public static Date firstDayOfMonth(Date workingDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, workingDate.getDay());
        cal.set(Calendar.MONTH, workingDate.getMonth());
        cal.set(Calendar.YEAR, workingDate.getYear());

        //set first date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date lastDayOfMonth(Date workingDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, workingDate.getDay());
        cal.set(Calendar.MONTH, workingDate.getMonth());
        cal.set(Calendar.YEAR, workingDate.getYear());

        //set last date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getPreviousDatePeriod(Date workingDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, workingDate.getDay());
        cal.set(Calendar.MONTH, workingDate.getMonth());
        cal.set(Calendar.YEAR, workingDate.getYear());

        //set previous month
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    private void initDbData(){
        try{
            Date current = currentSettings.GetWorkingPeriod();
            Date previous = getPreviousDatePeriod(current);
            currentSettings = _settingsManager.GetSettingsInfo();
            currentMonthDataSource = _dataManager.GetData(firstDayOfMonth(current), lastDayOfMonth(current), currentSettings.GetRegionId());
            previousMonthDataSource = _dataManager.GetData(firstDayOfMonth(previous), lastDayOfMonth(previous), currentSettings.GetRegionId());
            categorySource = _categoryManager.GetCategories();
            productSource = _productManager.GetProducts();
        }
        catch (Exception ex){
            Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - initDbData" + ex.getMessage());
        }
    }

    private double calcCategoryGeometricAverage(String categoryCode, List<Data> source){
        Category current = new Category();
        for(Category category : categorySource)
            if(category.GetCode().equals(categoryCode)){
                current = category;
                break;
            }

        List<Product> categoryProducts = new ArrayList<Product>();
        for(Product product : productSource)
            if(product.GetCategoryId() == current.GetId())
                categoryProducts.add(product);


        List<Data> categoryData = new ArrayList<Data>();
        for (Data data : source)
            for (Product categoryProduct : categoryProducts)
                if(categoryProduct.GetId() == data.GetProductId())
                    categoryData.add(data);

        double multiplyPrice = 1.0;
        for (Data data : categoryData)
            multiplyPrice *= data.GetPrice();

        return Math.pow(multiplyPrice, 1.0 / categoryData.size());
    }

    public List<elementaryCpi> getElementaryIndexList(String secondLevelCode){
        List<Category> lastLevelCategories = new ArrayList<Category>();
        List<elementaryCpi> result = new ArrayList<elementaryCpi>();

        for(Category category : categorySource)
            if(category.GetCode().contains(secondLevelCode) &&
                category.GetLevel() == Category.LEVEL_ITEM)
                lastLevelCategories.add(category);

        for (Category category : lastLevelCategories){
            double previousMonthAverage = calcCategoryGeometricAverage(category.GetCode(), previousMonthDataSource);
            double currentMonthAverage = calcCategoryGeometricAverage(category.GetCode(), currentMonthDataSource);

            result.add(new elementaryCpi(category.GetWeight(), currentMonthAverage/previousMonthAverage, category.GetCode()));
        }

        return result;
    }

    class elementaryCpi{
        public final double index;
        public final float weight;
        public String code;

        public elementaryCpi(float weight, double index, String code){
            this.weight = weight;
            this.index = index;
            this.code = code;
        }
    }
}
