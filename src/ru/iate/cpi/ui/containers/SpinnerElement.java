package ru.iate.cpi.ui.containers;

/**
 * Created by sanea on 11.05.15.
 */
public class SpinnerElement {
    public SpinnerElement(int elementId, String elementText){
        this.Id = elementId;
        this.Text = elementText;
    }

    public final int Id;
    public final String Text;

    @Override
    public String toString(){
        return this.Text;
    }
}
