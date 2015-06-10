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
    private List<Data> currentMonthDataSource = new ArrayList<Data>();;
    private List<Data> previousMonthDataSource = new ArrayList<Data>();;
    private List<Category> categorySource;
    private List<Product> productSource;

    public CpiCalculation(Context context){
        _dataManager = new DataManager(DatabaseFactory.Get());
        _settingsManager = new SettingsManager(DatabaseFactory.Get());
        _categoryManager = new CategoryManager(context, DatabaseFactory.Get());
        _productManager = new ProductManager(DatabaseFactory.Get());
    }

    public static Date FirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //set first date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date LastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //set last date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date GetPreviousDatePeriod(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //set previous month
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public void initDbData(){
        try{
            currentSettings = _settingsManager.GetSettingsInfo();

            Date current = currentSettings.GetWorkingPeriod();
            Date previous = GetPreviousDatePeriod(current);
            List<Data> regionData = _dataManager.GetData(currentSettings.GetRegionId());

            for (Data data : regionData){
                if(data.GetSubmitDate().compareTo(FirstDayOfMonth(current)) >= 0 && data.GetSubmitDate().compareTo(LastDayOfMonth(current)) <= 0)
                    currentMonthDataSource.add(data);
                if(data.GetSubmitDate().compareTo(FirstDayOfMonth(previous)) >= 0 && data.GetSubmitDate().compareTo(LastDayOfMonth(previous)) <= 0)
                    previousMonthDataSource.add(data);
            }

            Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - currentMonthDataSource " + currentMonthDataSource.size());
            Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - previousMonthDataSource " + previousMonthDataSource.size());

            categorySource = _categoryManager.GetCategories();
            productSource = _productManager.GetProducts();
        }
        catch (Exception ex){
            Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - initDbData" + ex.getMessage());
        }
    }

    private double calcCategoryGeometricAverage(String categoryCode, List<Data> source){
        //Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - calcCategoryGeometricAverage " + categoryCode);

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

        if(categoryData.isEmpty())
            return 0;

        double multiplyPrice = 1.0;
        for (Data data : categoryData)
            multiplyPrice *= data.GetPrice();

        return Math.pow(multiplyPrice, 1.0 / categoryData.size());
    }

    private List<CpiElement> getElementaryIndexList(String firstLevelCode){
        List<Category> lastLevelCategories = new ArrayList<Category>();
        List<CpiElement> result = new ArrayList<CpiElement>();

        for(Category category : categorySource)
            if(category.GetCode().contains(firstLevelCode) &&
                category.GetLevel() == Category.LEVEL_ITEM)
                lastLevelCategories.add(category);

        for (Category category : lastLevelCategories){
            double previousMonthAverage = calcCategoryGeometricAverage(category.GetCode(), previousMonthDataSource);
            double currentMonthAverage = calcCategoryGeometricAverage(category.GetCode(), currentMonthDataSource);

            if(previousMonthAverage != 0 || currentMonthAverage != 0){
                result.add(new CpiElement(category.GetWeight(), (currentMonthAverage/previousMonthAverage)*100, category.GetCode()));
            }
            else
                result.add(new CpiElement(category.GetWeight(), 100.0, category.GetCode()));
        }

        return result;
    }

    private CpiElement getAggregateFirstLevel(Category category){
        List<CpiElement> indexes = getElementaryIndexList(category.GetCode());


        double sumIndex = 0.0;
        double sumWeight = 0.0;
        for (CpiElement element : indexes){
            sumIndex += (element.weight * element.index);
            sumWeight += element.weight;
        }

        return new CpiElement(category.GetWeight(), sumIndex/sumWeight, category.GetCode());
    }


    public List<CpiElement> getAggregateFirstLevels(){
        Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - getAggregateFirstLevels");

        List<CpiElement> result = new ArrayList<CpiElement>();

        Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - getAggregateFirstLevels result " + result.size());

        for(Category category : categorySource)
            if(category.GetLevel() == Category.LEVEL_GROUP)
                result.add(getAggregateFirstLevel(category));

        return result;
    }

    public double getCpi(){
        Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - getCpi");

        List<CpiElement> firstLevelIndexes = getAggregateFirstLevels();
        double sumIndex = 0;
        double sumWeight = 0;
        for (CpiElement element : firstLevelIndexes){
            sumIndex += (element.weight * element.index);
            sumWeight += element.weight;
        }

        Log.d(LogTags.ERROR_PREFIX, "CpiCalculation - getCpi firstLevelIndexes " + firstLevelIndexes.size());

        return sumIndex/sumWeight;
    }
}
