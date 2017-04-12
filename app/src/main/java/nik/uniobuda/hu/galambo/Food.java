package nik.uniobuda.hu.galambo;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Adam on 2017. 04. 09..
 */

public class Food {
    private String nev;
    private int tapanyagmennyiseg;
    private int ar;
    private Drawable kep;

    public String getNev() {
        return nev;
    }

    public int getTapanyagmennyiseg() {
        return tapanyagmennyiseg;
    }

    public int getAr() {
        return ar;
    }

    public Drawable getKep() {
        return kep;
    }


    public Food(String nev, int tapanyagmennyiseg, int ar, Drawable kep) {
        this.nev = nev;
        this.tapanyagmennyiseg = tapanyagmennyiseg;
        this.ar = ar;
        this.kep = kep;
    }

}
