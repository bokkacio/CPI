package ru.iate.cpi.event;

/**
 * Created by sanea on 02.06.15.
 */
public class EditCategoryEvent {
    public EditCategoryEvent(float weight, int id){
        this.weight = weight;
        this.categoryId = id;
    }

    public final float weight;
    public final int categoryId;
}
