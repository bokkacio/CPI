package ru.iate.cpi.ui.containers;

/**
 * Created by vera on 20.05.15.
 */
public class ListViewElement {
    public ListViewElement(int elementId, String elementText){
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
