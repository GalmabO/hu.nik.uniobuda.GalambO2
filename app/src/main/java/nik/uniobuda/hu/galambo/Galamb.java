package nik.uniobuda.hu.galambo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * Created by Adam on 2017. 04. 03..
 */

//A nevelgetett galambunkat reprezentáló osztály
public class Galamb implements Serializable, Parcelable {
    private int teljesSzint;

    private String nev;

    private int kepId;

    private double egeszseg;
    private double fittseg;
    private double jollakottsag;
    private double kedelyallapot;
    private double intelligencia;
    private double kipihentseg;

    static final String[] ezeketcsinalhatja = new String[]{"alvás", "mozgás", "tanulás", "telefonozás", "olvasás", "lazulás", "zenehallgatás", "dolgozás"};

    private int mitcsinal;

    private long activityStartedDate;

    public static final int ENNYIPROPERTYVAN = 6;

    //ugyanannyi elemből áll mint ahány kaja van a storeban, a key megegygezik (kaja megnevezése), viszont itt a value, értéke az hogy az adott kajából mennyi van éppen megvásárolva.
    private HashMap kajamennyiseg;

    private int selectedFood;

    private int penz;

    protected Galamb(Parcel in) {


        teljesSzint = in.readInt();
        nev = in.readString();
        egeszseg = in.readDouble();
        fittseg = in.readDouble();
        jollakottsag = in.readDouble();
        kedelyallapot = in.readDouble();
        intelligencia = in.readDouble();
        kipihentseg = in.readDouble();
        penz = in.readInt();

        mitcsinal = in.readInt();

        selectedFood = in.readInt();

        activityStartedDate = in.readLong();

        kajamennyiseg = new HashMap();

        KajamennyisegIni();
    }


    public Galamb(String nev) {
        this.teljesSzint = 1;
        this.nev = nev;
        this.egeszseg = 0;
        this.fittseg = 0;
        this.intelligencia = 0;
        this.jollakottsag = 0;
        this.kedelyallapot = 0;
        this.kipihentseg = 0;
        penz = 0;
        mitcsinal = 0;
        activityStartedDate = Calendar.getInstance().getTime().getTime();
        selectedFood = -1;
        kajamennyiseg = new HashMap();
        KajamennyisegIni();
    }


    public static final Creator<Galamb> CREATOR = new Creator<Galamb>() {
        @Override
        public Galamb createFromParcel(Parcel in) {
            return new Galamb(in);
        }

        @Override
        public Galamb[] newArray(int size) {
            return new Galamb[size];
        }
    };

    public String getNev() {
        return nev;
    }

    public int getTeljesSzint() {
        return teljesSzint;
    }

    public void setTeljesSzint(int teljesSzint) {
        this.teljesSzint += teljesSzint;
    }

    public int getKepId() {
        return kepId;
    }

    public void setKepId(int kepId) {
        this.kepId = kepId;
    }

    public int getSelectedFood() {
        return selectedFood;
    }

    public void setSelectedFood(int selectedFood) {
        this.selectedFood = selectedFood;
    }

    public HashMap getKajamennyiseg() {
        return kajamennyiseg;
    }

    public void setKajamennyiseg(HashMap kajamennyiseg) {
        this.kajamennyiseg = kajamennyiseg;
    }

    public double getEgeszseg() {
        return egeszseg;
    }

    public void setEgeszseg(double egeszseg) {
        this.egeszseg += egeszseg;
    }

    public double getFittseg() {
        return fittseg;
    }

    public void setFittseg(double fittseg) {
        this.fittseg += fittseg;
    }

    public double getJollakottsag() {
        return jollakottsag;
    }

    public void setJollakottsag(double jollakottsag) {
        this.jollakottsag += jollakottsag;
    }

    public double getKedelyallapot() {
        return kedelyallapot;
    }

    public void setKedelyallapot(double kedelyallapot) {
        this.kedelyallapot += kedelyallapot;
    }

    public double getIntelligencia() {
        return intelligencia;
    }

    public void setIntelligencia(double intelligencia) {
        this.intelligencia += intelligencia;
    }

    public double getKipihentseg() {
        return kipihentseg;
    }

    public void setKipihentseg(double kipihentseg) {
        this.kipihentseg += kipihentseg;
    }

    public int getPenz() {
        return penz;
    }

    public void setPenz(int penz) {
        this.penz += penz;
    }

    public int getMitcsinal() {
        return mitcsinal;
    }

    public void setMitcsinal(int mitcsinal) {
        this.mitcsinal = mitcsinal;
    }

    private void KajamennyisegIni() {
        List<Food> seged = Store.getFoods();
        for (int i = 0; i < seged.size(); i++) {
            kajamennyiseg.put(seged.get(i).getName(), 0);
        }
    }

    public void Alvas(double time) {
        if (time < 480) //480--> 80 óra
            kipihentseg += (time * 2) / 20; //8óránál kevesebb alvás
        else {
            kipihentseg += (time * 1.5) / 20; //túlalvás
            intelligencia -= (time * 0.01) / 20; // sok alvástól butább lesz
        }
        fittseg -= (time * 0.08) / 20; //az alvástól veszéít a fittségéből (kis mennyiségben)
        jollakottsag -= time / 20; // eltelt idővel arányosan lesz éhes
    }

    public void Mozgas(Date changedTime, int step) {
        long diffInMs = changedTime.getTime() - activityStartedDate;
        double time = diffInMs / (60 * 5000) % 60;

        fittseg += (time * 1.5) / 400 + step / 5000;
        kipihentseg -= (time * 0.5) / 400 + step / 5000;
        kedelyallapot += (time * 0.5) / 400 + step / 5000;
        jollakottsag -= time / 400 + step / 5000;
        egeszseg += (time * 0.2) / 400 + step / 5000;
    }

    public void Tanulas(double time) {
        intelligencia += (time * 2) / 20;
        Random rnd = new Random();
        kedelyallapot += (time * rnd.nextInt(3 - 2) + 2 - rnd.nextInt(3 - 2) + 2) / 20; //random hogy jó-e tanulni
        fittseg -= (time * 0.08) / 20;
        kipihentseg -= (time * 0.8) / 20;
        jollakottsag -= time / 20;
        penz += (time) / 20;
    }

    public void Telefonozas(double time) {
        kedelyallapot += (time * 1.01) / 20;
        Random rnd = new Random();
        intelligencia += (time * rnd.nextInt(3 - 2) + 2 - rnd.nextInt(3 - 2) + 2) / 20;
        fittseg -= (time * 0.08) / 20;
        kipihentseg -= (time * 0.6) / 20;
        egeszseg -= (time * 0.1) / 20;
        jollakottsag -= (time) / 20;
    }

    public void Olvasas(double time) {
        intelligencia += (time * 1.2) / 20;
        kipihentseg -= (time * 0.8) / 20;
        fittseg -= (time * 0.08) / 20;
        jollakottsag -= (time) / 20;
    }

    public void Lazulas(double time) {
        kedelyallapot += (time * 2) / 20;
        kipihentseg -= (time * 0.3) / 20;
        fittseg -= (time * 0.08) / 20;
        jollakottsag -= (time) / 20;
        egeszseg -= (time * 0.1) / 20;
    }

    public void ZeneHallgatas(double time) {
        kedelyallapot += (time * 1.3) / 20;
        kipihentseg -= (time * 0.2) / 20;
        fittseg -= (time * 0.08) / 20;
        jollakottsag -= (time) / 20;
    }

    public void Dolgozas(double time) {
        kedelyallapot -= (time * 0.5) / 20;
        kipihentseg -= (time * 0.5) / 20;
        fittseg -= (time * 0.5) / 20;
        egeszseg -= (time * 0.1) / 20;
        intelligencia -= (time * 0.05) / 20;
        jollakottsag -= (time) / 20;
        penz += (time * 4) / 20;
    }

    public void Eves(int valasztottkajataplalekmennyisege) {
        kipihentseg -= 0.2;
        fittseg -= 0.2;
        egeszseg += 0.2;
        jollakottsag += valasztottkajataplalekmennyisege * 0.75;
    }

    public void DoveActivityChange(int activityID, Date changedTime) {

        long diffInMs = changedTime.getTime() - activityStartedDate;
        long powerOfChangedTime = diffInMs / (60 * 1000) % 60;
        //ez lesz az az érték ami meghatározza hogy milyen szintán változnak az egyes propertyk értékei.

        DoveActivityChanger(activityID, powerOfChangedTime);

        activityStartedDate = changedTime.getTime();

    }

    private void DoveActivityChanger(int which, long time) {
        switch (which) {
            case 0:
                Alvas(time);
                break;
            case 2:
                Tanulas(time);
                break;
            case 3:
                Telefonozas(time);
                break;
            case 4:
                Olvasas(time);
                break;
            case 5:
                Lazulas(time);
                break;
            case 6:
                ZeneHallgatas(time);
                break;
            case 7:
                Dolgozas(time);
                break;
        }
    }


    public void KajaVasarlas(String nev) {
        kajamennyiseg.put(nev, (int) kajamennyiseg.get(nev) + 1);
    }

    public void KajaFogyasztas(String nev) {
        kajamennyiseg.put(nev, (int) kajamennyiseg.get(nev) - 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teljesSzint);
        dest.writeString(nev);
        dest.writeDouble(egeszseg);
        dest.writeDouble(fittseg);
        dest.writeDouble(jollakottsag);
        dest.writeDouble(kedelyallapot);
        dest.writeDouble(intelligencia);
        dest.writeDouble(kipihentseg);
        dest.writeInt(penz);
        dest.writeInt(mitcsinal);
        dest.writeInt(selectedFood);
        dest.writeLong(activityStartedDate);
    }

}
