package nik.uniobuda.hu.galambo;


import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Adam on 2017. 04. 03..
 */

public abstract class Store
{
    //a key a kaja neve, a value az adott kaja ára
    private static Dictionary cikkek = new Hashtable();

    static {
        Dictionary seged = new Hashtable();
        seged.put("Bread",10);
        seged. put("Coffee",8);
        seged.put("ChickenNugget",20);
        seged.put("Corn",15);

        seged.put("valami",0);
        cikkek =seged;
    }

    public static Dictionary getCikkek() {
        return cikkek;
    }

}
