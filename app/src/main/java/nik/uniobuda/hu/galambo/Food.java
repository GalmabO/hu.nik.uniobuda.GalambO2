package nik.uniobuda.hu.galambo;

/**
 * Created by Adam on 2017. 04. 09..
 */

//Ételt reprezentáló osztály
public class Food {
    private String name;
    private int nutrient; //Tápanyagtartalom
    private int cost;
    private int imageID;

    public String getName() {
        return name;
    }

    public int getNutrient() {
        return nutrient;
    }

    public int getCost() {
        return cost;
    }

    public int getImageID() {
        return imageID;
    }

    public Food(String name, int nutrient, int cost, int imageID) {
        this.name = name;
        this.nutrient = nutrient;
        this.cost = cost;
        this.imageID = imageID;
    }
}
