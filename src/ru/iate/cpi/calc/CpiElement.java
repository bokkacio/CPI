package ru.iate.cpi.calc;

/**
 * Created by sanea on 10.06.15.
 */
public class CpiElement {
    public final float weight;
    public final double index;
    public final String code;
    public final String title;

    public CpiElement(float weight, double index, String code, String title){
        this.weight = weight;
        this.index = index;
        this.code = code;
        this.title = title;
    }
}