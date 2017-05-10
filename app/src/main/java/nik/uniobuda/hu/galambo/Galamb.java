package nik.uniobuda.hu.galambo;

import android.content.Context;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Adam on 2017. 04. 03..
 */

//A nevelgetett galambunkat reprezentáló osztály
public class Galamb implements Serializable {

    //region consts
    //Milyen gyorsan változzanak a tulajdonságok
    final int TIMECORRETION = 10;

    //Lehetséges tevékenységek
    static final String[] ezeketcsinalhatja =
            new String[]{"Alvás", "Mozgás", "Tanulás", "Telefonozás", "Olvasás", "Lazulás", "Zenehallgatás", "Munka"};
    public static final int NUMOFPROPERTIES = 6;
    //endregion

    //region fields
    private String name;
    private int imageID;
    private double health;
    private double fitness;
    private double satiety;
    private double mood;
    private double intelligence;
    private double relaxed;
    private int currentActivity;
    private List<StepCounterLog> previousSteps;
    private int selectedFood;
    private int money;
    private DateTime activityStartedDate;
    private DateTime trueStartedDate;

    public int actualStep;
    //ugyanannyi elemből áll mint ahány kaja van a storeban, a key megegygezik
    // (kaja megnevezése), viszont itt a value, értéke az hogy az adott kajából mennyi van éppen megvásárolva.
    private HashMap<String, Integer> foodQuantity;
    //endregion

    //region getters and setters
    public void setActivityStartedDate(DateTime startedDate) {
        activityStartedDate = startedDate;
    }

    public void setPreviousSteps(List<StepCounterLog> previousSteps) {
        this.previousSteps = previousSteps;
    }

    public String getName() {
        return name;
    }

    public List<StepCounterLog> getPreviousSteps() {
        return previousSteps;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getSelectedFood() {
        return selectedFood;
    }

    public void setSelectedFood(int selectedFood) {
        this.selectedFood = selectedFood;
    }

    public HashMap getFoodQuantity() {
        return foodQuantity;
    }

    public double getHealth() {
        return health;
    }

    public double getFitness() {
        return fitness;
    }

    public double getSatiety() {
        return satiety;
    }

    public double getMood() {
        return mood;
    }

    public double getIntelligence() {
        return intelligence;
    }

    public double getRelaxed() {
        return relaxed;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money += money;
    }

    public int getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(int currentActivity) {
        this.currentActivity = currentActivity;
    }

    public int getActualStep() {
        return actualStep;
    }

    public void setActualStep(int actualStep) {
        this.actualStep = actualStep;
    }
//endregion

    //region constructor
    public Galamb(String name) {
        this.name = name;
        this.health = 0;
        this.fitness = 0;
        this.intelligence = 0;
        this.satiety = 0;
        this.mood = 0;
        this.relaxed = 0;
        money = 200; //ajándék
        currentActivity = 0;
        activityStartedDate = DateTime.now();
        selectedFood = -1;
        foodQuantity = new HashMap();
        previousSteps = new ArrayList<>();
        foodQuantityInit();
        actualStep = 0;
    }
    //endregion

    //region Methods

    // inicializálja az ételek hashmapjét,
    // létrehozza betesz minden ételt 0 mennyiséggel
    private void foodQuantityInit() {
        List<Food> seged = Store.getFoods();
        for (int i = 0; i < seged.size(); i++) {
            foodQuantity.put(seged.get(i).getName(), 0);
        }
    }

    // hozzáad egy új bejegyzést az előző lépsészámlálós mérések listájához
    public void addPreviousSteps(StepCounterLog previousStep) {
        if (previousStep != null) {
            if (previousSteps == null) {
                previousSteps = new ArrayList<StepCounterLog>();
            }
            previousSteps.add(previousStep);
        }
    }

    //Alvás aktivitás végén hivódik meg
    public void Sleep(double time) {
        if (time < 480) //480--> 80 óra
            relaxed += (time * 2) / TIMECORRETION; //8óránál kevesebb alvás
        else {
            relaxed += (time * 1.5) / TIMECORRETION; //túlalvás
            intelligence -= (time * 0.01) / TIMECORRETION; // sok alvástól butább lesz
        }
        fitness -= (time * 0.08) / TIMECORRETION; //az alvástól veszéít a fittségéből (kis mennyiségben)
        satiety -= time / TIMECORRETION; // eltelt idővel arányosan lesz éhes
    }

    //Mozgás után hívjuk meg
    public void Move(DateTime changedTime, int step, Boolean isJustRefresh) {

        //Ha frissít a swipebar miatt akkor ne induljon újra az idő mérés, ezért ha van swipe akkor az első
        //nél (amikor még null a date) betesszük a kezdő időpontot egy másik változóba,
        //és ha tényleg befejzte a mozgást ezzel számolunk
        if (trueStartedDate == null && isJustRefresh) {
            trueStartedDate = activityStartedDate;
        }
        double time = TimeDiffInMinute(changedTime);
        fitness += (time * 1.5) / 400 + step / 500;
        relaxed -= (time * 0.5) / 400 + step / 500;
        mood += (time * 0.5) / 400 + step / 500;
        satiety -= time / 400 + step / 500;
        health += (time * 0.2) / 400 + step / 500;
        //Hozzáadás a mozgásnaplóhoz
        if (!isJustRefresh) {
            Period p = trueStartedDate != null ? new Period(trueStartedDate, changedTime)
                    : new Period(activityStartedDate, changedTime);
            this.addPreviousSteps(new StepCounterLog(step, p));
            this.trueStartedDate = null;
        }
        //ide külön kell mert a mozgást külön kezeljük
        activityStartedDate = changedTime;
    }

    public void Learning(double time) {
        intelligence += (time * 2) / TIMECORRETION;
        Random rnd = new Random();
        mood += (time * rnd.nextInt(3 - 2) + 2 - rnd.nextInt(3 - 2) + 2) / TIMECORRETION; //random hogy jó-e tanulni
        fitness -= (time * 0.08) / TIMECORRETION;
        relaxed -= (time * 0.8) / TIMECORRETION;
        satiety -= time / TIMECORRETION;
        money += time;
    }

    public void GamingOnPhone(double time) {
        mood += (time * 1.01) / TIMECORRETION;
        Random rnd = new Random();
        intelligence += (time * rnd.nextInt(3 - 2) + 2 - rnd.nextInt(3 - 2) + 2) / TIMECORRETION;
        fitness -= (time * 0.08) / TIMECORRETION;
        relaxed -= (time * 0.6) / TIMECORRETION;
        health -= (time * 0.1) / TIMECORRETION;
        satiety -= (time) / TIMECORRETION;
    }

    public void Read(double time) {
        intelligence += (time * 1.2) / TIMECORRETION;
        relaxed -= (time * 0.8) / TIMECORRETION;
        fitness -= (time * 0.08) / TIMECORRETION;
        satiety -= (time) / TIMECORRETION;
    }

    public void Rest(double time) {
        mood += (time * 2) / TIMECORRETION;
        relaxed -= (time * 0.3) / TIMECORRETION;
        fitness -= (time * 0.08) / TIMECORRETION;
        satiety -= (time) / TIMECORRETION;
        health -= (time * 0.1) / TIMECORRETION;
    }

    public void ListenToMusic(double time) {
        mood += (time * 1.3) / TIMECORRETION;
        relaxed -= (time * 0.2) / TIMECORRETION;
        fitness -= (time * 0.08) / TIMECORRETION;
        satiety -= (time) / TIMECORRETION;
    }

    public void Work(double time, Context context) {
        mood -= (time * 0.5) / TIMECORRETION;
        relaxed -= (time * 0.5) / TIMECORRETION;
        fitness -= (time * 0.5) / TIMECORRETION;
        health -= (time * 0.1) / TIMECORRETION;
        intelligence -= (time * 0.05) / TIMECORRETION;
        satiety -= (time) / TIMECORRETION;
        double salary = time * 100;
        money += (salary);
        Toast.makeText(context, salary + " dollárt kerestél!", Toast.LENGTH_SHORT).show();
    }

    public void Eat(int nutritionOfChangedFood) {
        relaxed -= 0.2;
        fitness -= 0.2;
        health += 0.2;
        satiety += nutritionOfChangedFood;
    }

    public void DoveActivityChange(DateTime changedTime, Context contetxt) {

        double time = TimeDiffInMinute(changedTime);
        DoveActivityChanger(time, contetxt);
        //új aktivitást kezdtünk
        activityStartedDate = changedTime;
    }

    //Aktivitás hatásának szimulálása, bemenet az eltelt idő
    // (a mozgás külön van kezelve a mainactivityből)
    private void DoveActivityChanger(double time, Context context) {
        switch (currentActivity) {
            case 0:
                Sleep(time);
                break;
            case 2:
                Learning(time);
                break;
            case 3:
                GamingOnPhone(time);
                break;
            case 4:
                Read(time);
                break;
            case 5:
                Rest(time);
                break;
            case 6:
                ListenToMusic(time);
                break;
            case 7:
                Work(time, context);
                break;
        }
    }

    //segédfüggvény double tipusban adja vissza két időpont közt eltelt perceket
    private double TimeDiffInMinute(DateTime changedTime) {
        Period p = new Period(activityStartedDate, changedTime);
        return p.toStandardMinutes().getMinutes() + (((double) p.toStandardSeconds().getSeconds() % 60) / 60);
    }

    public void BuyFood(String nev) {
        foodQuantity.put(nev, foodQuantity.get(nev) + 1);
    }

    public void EatFood(String nev) {
        foodQuantity.put(nev, foodQuantity.get(nev) - 1);
    }
    //endregion
}
