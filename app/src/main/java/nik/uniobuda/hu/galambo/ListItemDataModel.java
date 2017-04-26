package nik.uniobuda.hu.galambo;

/**
 * Created by Siket Kristóf on 2017.04.17..
 */

public class ListItemDataModel {
    private String prop;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double value;

    public ListItemDataModel(String prop, double value) {
        this.prop = prop;
        this.value = value;
    }

}
