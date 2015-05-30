package ru.iate.cpi.ui.containers;

import ru.iate.cpi.db.table.Category;

/**
 * Created by sanea on 30.05.15.
 */
public class CategoryListContainer {
    public CategoryListContainer(Category category){
        this.category = category;
    }

    public final Category category;

    @Override
    public String toString(){
        return String.format("%s %s", this.category.GetCode(), this.category.GetTitle());
    }
}
