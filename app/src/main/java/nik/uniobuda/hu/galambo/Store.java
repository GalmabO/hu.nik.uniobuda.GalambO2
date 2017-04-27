package nik.uniobuda.hu.galambo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017. 04. 03..
 */

public abstract class Store
{
    //a key a kaja neve, a value az adott kaja ára
    private static List<Food> cikkek = new ArrayList<>();

    static {
        cikkek.add(new Food("Kenyér",1,10,(R.drawable.kenyer)));
        cikkek.add(new Food("Kávé",1,8,(R.drawable.kave)));
        cikkek.add(new Food("Csirkemell",5,20,(R.drawable.csirke)));
        cikkek.add(new Food("Kukorica",3,15,(R.drawable.kukorica)));
        cikkek.add(new Food("Alma",2,8,(R.drawable.alma)));
        cikkek.add(new Food("Ananász",3,15,(R.drawable.ananasz)));
        cikkek.add(new Food("Görögdinnye",1,6,(R.drawable.dinnye)));
        cikkek.add(new Food("Eper",2,10,(R.drawable.eper)));
        cikkek.add(new Food("Fánk",2,15,(R.drawable.fank)));
        cikkek.add(new Food("Hamburger",5,26,(R.drawable.hamburger)));
        cikkek.add(new Food("Hotdog",3,18,(R.drawable.hotdog)));
        cikkek.add(new Food("Narancs",2,12,(R.drawable.narancs)));
        cikkek.add(new Food("Paradicsom",1,4,(R.drawable.paradicsom)));
        cikkek.add(new Food("Uborka",1,4,(R.drawable.uborka)));
        cikkek.add(new Food("Saláta",2,8,(R.drawable.salata)));
        cikkek.add(new Food("Sültkrumpli",3,20,(R.drawable.sultkrumli)));
        cikkek.add(new Food("Torta",4,15,(R.drawable.torra)));
        cikkek.add(new Food("Pizza",5,25,(R.drawable.pizza)));


    }

    public static List<Food> getCikkek() {
        return cikkek;
    }

}
