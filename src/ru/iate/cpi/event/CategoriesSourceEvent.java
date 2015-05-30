package ru.iate.cpi.event;

import ru.iate.cpi.db.table.Category;

import java.util.List;

/**
 * Created by sanea on 30.05.15.
 */
public class CategoriesSourceEvent {
    public CategoriesSourceEvent(List<Category> categories){
        this.Categories = categories;
    }

    public final List<Category> Categories;
}
