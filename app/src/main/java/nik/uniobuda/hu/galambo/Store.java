package nik.uniobuda.hu.galambo;

import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017. 04. 03..
 */

public abstract class Store {
    //A bolt árukészletét jelentő tömböt tölti fel ami nem változik futási időben.
    private static List<Food> foods;
    //Ezt is ide ne kelljen a minben mindig inicializálni
    //mintha hashmap lenne csak optimálisabb
    private static SparseIntArray ImagesToActivities;

    static {
        foods = new ArrayList<>();
        foods.add(new Food("Kenyér", 1, 10, (R.drawable.kenyer)));
        foods.add(new Food("Kávé", 1, 8, (R.drawable.kave)));
        foods.add(new Food("Csirkemell", 5, 20, (R.drawable.csirke)));
        foods.add(new Food("Kukorica", 3, 15, (R.drawable.kukorica)));
        foods.add(new Food("Alma", 2, 8, (R.drawable.alma)));
        foods.add(new Food("Ananász", 3, 15, (R.drawable.ananasz)));
        foods.add(new Food("Görögdinnye", 1, 6, (R.drawable.dinnye)));
        foods.add(new Food("Eper", 2, 10, (R.drawable.eper)));
        foods.add(new Food("Fánk", 2, 15, (R.drawable.fank)));
        foods.add(new Food("Hamburger", 5, 26, (R.drawable.hamburger)));
        foods.add(new Food("Hotdog", 3, 18, (R.drawable.hotdog)));
        foods.add(new Food("Narancs", 2, 12, (R.drawable.narancs)));
        foods.add(new Food("Paradicsom", 1, 4, (R.drawable.paradicsom)));
        foods.add(new Food("Uborka", 1, 4, (R.drawable.uborka)));
        foods.add(new Food("Saláta", 2, 8, (R.drawable.salata)));
        foods.add(new Food("Sültkrumpli", 3, 20, (R.drawable.sultkrumli)));
        foods.add(new Food("Torta", 4, 15, (R.drawable.torra)));
        foods.add(new Food("Pizza", 5, 25, (R.drawable.pizza)));

        ImagesToActivities = new SparseIntArray();
        ImagesToActivities.put(0, R.drawable.alszik);
        ImagesToActivities.put(1, R.drawable.sportol);
        ImagesToActivities.put(2, R.drawable.tanul);
        ImagesToActivities.put(3, R.drawable.telefonozik);
        ImagesToActivities.put(4, R.drawable.olvas);
        ImagesToActivities.put(5, R.drawable.lazul);
        ImagesToActivities.put(6, R.drawable.zenethallgat);
        ImagesToActivities.put(7, R.drawable.dolgozas);
    }

    public static List<Food> getFoods() {
        return foods;
    }

    public static SparseIntArray getImagesToActivities() {
        return ImagesToActivities;
    }
}
