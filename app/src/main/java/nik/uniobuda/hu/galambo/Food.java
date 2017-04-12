package nik.uniobuda.hu.galambo;

/**
 * Created by Adam on 2017. 04. 09..
 */

public class Food {
    String nev;
    int tapanyagmennyiseg;
    int ar;

    public String getNev() {
        return nev;
    }

    public int getTapanyagmennyiseg() {
        return tapanyagmennyiseg;
    }

    public int getAr() {
        return ar;
    }

    public Food(String nev, int tapanyagmennyiseg, int ar) {
        this.nev = nev;
        this.tapanyagmennyiseg = tapanyagmennyiseg;
        this.ar = ar;
    }

}
