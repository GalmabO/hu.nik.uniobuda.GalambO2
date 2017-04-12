package nik.uniobuda.hu.galambo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Adam on 2017. 04. 03..
 */

public class Galamb implements Serializable, Parcelable
{
    private int teljesSzint;

    private String nev;

    private double egeszseg;
    private double fittseg;
    private double jollakottsag;
    private double kedelyallapot;
    private double intelligencia;
    private double kipihentseg;

    public static final int ENNYIPROPERTYVAN = 6;

    //ugyanannyi elemből áll mint ahány kaja van a storeban, a key megegygezik (kaja megnevezése), viszont itt a value, értéke az hogy az adott kajából mennyi van éppen megvásárolva.
    private Dictionary kajamennyiseg;

    private int penz;

    private boolean VanETojas;

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
        VanETojas = in.readByte() != 0;

        KajamennyisegIni();
    }

    public Galamb(String nev) {

        this.teljesSzint = 1;

        this.nev = nev;

        this.egeszseg=0;
        this.fittseg=0;
        this.intelligencia=0;
        this.jollakottsag = 0;
        this.kedelyallapot=0;
        this.kipihentseg =0;

        penz = 0;

        VanETojas = false;

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

    public Dictionary getKajamennyiseg() {
        return kajamennyiseg;
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



    private void KajamennyisegIni()
    {
        kajamennyiseg = new Hashtable();
        List<Food> seged = Store.getCikkek();
        for (int i = 0; i < seged.size(); i++) {
            kajamennyiseg.put(seged.get(i).getNev(),0);
        }
    }

    public void Alvas(double time)
    {
        if(time < 8)
            kipihentseg+=time*1.5; //8óránál kevesebb alvás
        else
        {
            kipihentseg+=time*0.8; //túlalvás
            intelligencia-=time*0.01; // sok alvástól butább lesz
        }
        fittseg-=time*0.08; //az alvástól veszéít a fittségéből (kis mennyiségben)
        jollakottsag-=time; // eltelt idővel arányosan lesz éhes
    }

    public void Mozgas(double time)
    {
        fittseg +=time*1.5;
        kipihentseg -= time*0.5;
        kedelyallapot+=time*0.5;
        jollakottsag-=time;
    }

    public void Tanulas(double time)
    {
        intelligencia+=time*2;
        Random rnd = new Random();
        kedelyallapot+=time*rnd.nextInt(3-2)+2-rnd.nextInt(3-2)+2; //random hogy jó-e tanulni
        fittseg-=time*0.08;
        kipihentseg -= time*0.8;
        jollakottsag-=time;
    }

    public void Filmezes(double time)
    {
        kedelyallapot+=time*1.01;
        Random rnd = new Random();
        intelligencia+=time*rnd.nextInt(3-2)+2-rnd.nextInt(3-2)+2;
        fittseg-=time*0.08;
        kipihentseg -= time*0.6;
        jollakottsag-=time;
    }

    public void Olvasas(double time)
    {
        intelligencia+=time*1.2;
        kipihentseg -= time*0.8;
        fittseg-=time*0.08;
        jollakottsag-=time;
    }

    public void Lazulas(double time)
    {
        kedelyallapot += time*2;
        kipihentseg -= time*0.3;
        fittseg-=time*0.08;
        jollakottsag-=time;
    }

    public void ZeneHallgatas(double time)
    {
        kedelyallapot += time*1.3;
        kipihentseg -= time*0.2;
        fittseg-=time*0.08;
        jollakottsag-=time;
    }

    public void Eves(int valasztottkajataplalekmennyisege)
    {
        kipihentseg -= 0.2;
        fittseg-=0.2;
        jollakottsag+=valasztottkajataplalekmennyisege;
    }

    public boolean TojasRakas() //egyszerre csak egy tojás lehet lerakva
    {
        if(!VanETojas)
        {
            VanETojas = true;
            return true;
        }
        return false;
    }

    public void KajaVasarlas(String nev)
    {
        kajamennyiseg.put(nev,(int)kajamennyiseg.get(nev)+1);
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
        dest.writeByte((byte) (VanETojas ? 1 : 0));
    }
}
