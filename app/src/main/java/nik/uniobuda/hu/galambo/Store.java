package nik.uniobuda.hu.galambo;


import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Adam on 2017. 04. 03..
 */

public abstract class Store
{
    //a key a kaja neve, a value az adott kaja ára
    private static List<Food> cikkek = new ArrayList<>();

    static {
        cikkek.add(new Food("Kenyér",1,10));
        cikkek.add(new Food("Kávé",1,8));
        cikkek.add(new Food("Csirkemell",5,20));
        cikkek.add(new Food("Kukorica",3,15));
        cikkek.add(new Food("valami",0,0));
    }

    public static List<Food> getCikkek() {
        return cikkek;
    }

}
