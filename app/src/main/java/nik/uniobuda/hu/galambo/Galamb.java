package nik.uniobuda.hu.galambo;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Adam on 2017. 04. 03..
 */

public class Galamb
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

    //ugyanannyi elemből áll mint ahány kaja van a storeban, a key megegygezik (kaja megnevezése), viszont itt a value, hogy az adott kajából mennyi van éppen megvásárolva.
    Dictionary kajamennyiseg;

    private int penz;

    private boolean VanETojas;

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

        kajamennyiseg = new Hashtable();
        Dictionary seged = Store.getCikkek();
        for (Enumeration e = seged.keys(); e.hasMoreElements();) {
            kajamennyiseg.put(e.nextElement(),0);
        }
    }

    public void Alvas(int time)
    {

    }

    public void Mozgas(int time)
    {

    }

    public void Tanulas(int time)
    {

    }

    public void Filmezes(int time)
    {

    }

    public void Olvasas(int time)
    {

    }

    public void Lazulas(int time)
    {

    }

    public void ZeneHallgatas(int time)
    {

    }


    public void Eves()
    {

    }

    public boolean TojasRakas()
    {
        if(!VanETojas)
        {
            VanETojas = true;
            return true;
        }
        return false;
    }


}