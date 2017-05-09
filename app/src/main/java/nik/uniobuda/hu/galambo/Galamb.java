package nik.uniobuda.hu.galambo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * Created by Adam on 2017. 04. 03..
 */

//A nevelgetett galambunkat reprezentáló osztály
public class Galamb implements Serializable  {
    private String nev;

    final int TIMECORRETION = 5;

    private int kepId;

    private double egeszseg;
    private double fittseg;
    private double jollakottsag;
    private double kedelyallapot;
    private double intelligencia;
    private double kipihentseg;

    static final String[] ezeketcsinalhatja = new String[]{"Alvás", "Mozgás", "Tanulás", "Telefonozás", "Olvasás", "Lazulás", "Zenehallgatás", "Munka"};

    private int mitcsinal;

    private DateTime activityStartedDate;

    public static final int ENNYIPROPERTYVAN = 6;

    //ugyanannyi elemből áll mint ahány kaja van a storeban, a key megegygezik (kaja megnevezése), viszont itt a value, értéke az hogy az adott kajából mennyi van éppen megvásárolva.
    private HashMap<String ,Integer> kajamennyiseg;

    private int selectedFood;

    private int penz;

    public void setPreviousSteps(List<StepCounterLog> previousSteps) {
        this.previousSteps = previousSteps;
    }

    private List<StepCounterLog> previousSteps;



    public Galamb(String nev) {
        this.nev = nev;
        this.egeszseg = 0;
        this.fittseg = 0;
        this.intelligencia = 0;
        this.jollakottsag = 0;
        this.kedelyallapot = 0;
        this.kipihentseg = 0;
        penz = 200; //ajándék
        mitcsinal = 0;
        activityStartedDate = DateTime.now();
        selectedFood = -1;
        kajamennyiseg = new HashMap();

        previousSteps = new ArrayList<>();
        KajamennyisegIni();
    }


    public String getNev() {
        return nev;
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
            kajamennyiseg.put(seged.get(i).getName(),0);
        }
    }

    public List<StepCounterLog> getPreviousSteps() {
        return previousSteps;
    }

    public void addPreviousSteps(StepCounterLog previousStep) {
       if (previousStep!=null)
       {
           if (previousSteps==null)
           {
               previousSteps = new ArrayList<StepCounterLog>();
           }
           previousSteps.add(previousStep);
       }
    }


    public void Alvas(double time) {
        if (time < 480) //480--> 80 óra
            kipihentseg += (time * 2) /TIMECORRETION; //8óránál kevesebb alvás
        else {
            kipihentseg += (time * 1.5) /TIMECORRETION; //túlalvás
            intelligencia -= (time * 0.01) /TIMECORRETION; // sok alvástól butább lesz
        }
        fittseg -= (time * 0.08) /TIMECORRETION; //az alvástól veszéít a fittségéből (kis mennyiségben)
        jollakottsag -= time/TIMECORRETION ; // eltelt idővel arányosan lesz éhes
    }

    public void Mozgas(DateTime changedTime, int step) {
        Period p = new Period(activityStartedDate,changedTime);
        long time = p.getSeconds()/60;
//        long diffInMs =  - activityStartedDate;
//        double time = diffInMs /( 60 * 1000)%60 ;


        fittseg += (time * 1.5) / 400 + step / 500;
        kipihentseg -= (time * 0.5) / 400 + step / 500;
        kedelyallapot += (time * 0.5) / 400 + step / 500;
        jollakottsag -= time / 400 + step / 500;
        egeszseg += (time * 0.2) / 400 + step / 500;

        activityStartedDate = changedTime;
        this.addPreviousSteps(new StepCounterLog(step, p.getHours(),p.getMinutes(),p.getSeconds()));
    }

    public void Tanulas(double time) {
        intelligencia += (time * 2) /TIMECORRETION;
        Random rnd = new Random();
        kedelyallapot += (time * rnd.nextInt(3 - 2) + 2 - rnd.nextInt(3 - 2) + 2) /TIMECORRETION; //random hogy jó-e tanulni
        fittseg -= (time * 0.08) /TIMECORRETION;
        kipihentseg -= (time * 0.8) /TIMECORRETION;
        jollakottsag -= time /TIMECORRETION;
        penz += time;
    }

    public void Telefonozas(double time) {
        kedelyallapot += (time * 1.01) /TIMECORRETION;
        Random rnd = new Random();
        intelligencia += (time * rnd.nextInt(3 - 2) + 2 - rnd.nextInt(3 - 2) + 2)/TIMECORRETION ;
        fittseg -= (time * 0.08)/TIMECORRETION;
        kipihentseg -= (time * 0.6)/TIMECORRETION;
        egeszseg -= (time * 0.1)/TIMECORRETION;
        jollakottsag -= (time)/TIMECORRETION;
    }

    public void Olvasas(double time) {
        intelligencia += (time * 1.2) /TIMECORRETION;
        kipihentseg -= (time * 0.8) /TIMECORRETION;
        fittseg -= (time * 0.08)/TIMECORRETION;
        jollakottsag -= (time)/TIMECORRETION;
    }

    public void Lazulas(double time) {
        kedelyallapot += (time * 2)/TIMECORRETION;
        kipihentseg -= (time * 0.3)/TIMECORRETION;
        fittseg -= (time * 0.08) /TIMECORRETION;
        jollakottsag -= (time) /TIMECORRETION;
        egeszseg -= (time * 0.1) /TIMECORRETION;
    }

    public void ZeneHallgatas(double time) {
        kedelyallapot += (time * 1.3) /TIMECORRETION;
        kipihentseg -= (time * 0.2) /TIMECORRETION;
        fittseg -= (time * 0.08) /TIMECORRETION;
        jollakottsag -= (time)/TIMECORRETION;
    }

    public void Dolgozas(double time) {
        kedelyallapot -= (time * 0.5)/TIMECORRETION;
        kipihentseg -= (time * 0.5) /TIMECORRETION;
        fittseg -= (time * 0.5)/TIMECORRETION;
        egeszseg -= (time * 0.1)/TIMECORRETION;
        intelligencia -= (time * 0.05)/TIMECORRETION;
        jollakottsag -= (time)/TIMECORRETION;
        penz += (time * 4);
    }

    public void Eves(int valasztottkajataplalekmennyisege) {
        kipihentseg -= 0.2;
        fittseg -= 0.2;
        egeszseg += 0.2;
        jollakottsag += valasztottkajataplalekmennyisege;
    }

        public void DoveActivityChange(int activityID, DateTime changedTime) {

        Period p = new Period(activityStartedDate,changedTime);
        long time = p.getSeconds()/60;

        DoveActivityChanger(activityID, time);

        activityStartedDate = changedTime;

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
}
