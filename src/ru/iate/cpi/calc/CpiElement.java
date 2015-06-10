package ru.iate.cpi.calc;

/**
 * Created by sanea on 10.06.15.
 */
public class CpiElement {
    public final double index;
    public final float weight;
    public String code;

    public CpiElement(float weight, double index, String code){
        this.weight = weight;
        this.index = index;
        this.code = code;
    }
}