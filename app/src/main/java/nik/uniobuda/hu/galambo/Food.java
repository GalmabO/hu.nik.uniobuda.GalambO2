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
    private int kepID;

    public String getNev() {
        return nev;
    }

    public int getTapanyagmennyiseg() {
        return tapanyagmennyiseg;
    }

    public int getAr() {
        return ar;
    }

    public int getKepID() {
        return kepID;
    }


    public Food(String nev, int tapanyagmennyiseg, int ar, int kepID) {
        this.nev = nev;
        this.tapanyagmennyiseg = tapanyagmennyiseg;
        this.ar = ar;
        this.kepID = kepID;
    }

}
