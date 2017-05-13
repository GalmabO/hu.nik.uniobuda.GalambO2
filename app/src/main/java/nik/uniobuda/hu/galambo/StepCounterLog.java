package nik.uniobuda.hu.galambo;

import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.MonthDay;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodPrinter;

import java.io.Serializable;
import java.math.RoundingMode;

/**
 * Created by Adam on 2017. 05. 05..
 */

public class StepCounterLog implements Serializable {
    int stepCount;
    Period p;

    public StepCounterLog() {
        p = new Period();
    }

    public StepCounterLog(int stepCount, Period p) {
        this.stepCount = stepCount;
        this.p = p;
    }

    public int getStepCount() {
        return stepCount;
    }

    public String getTimeInFormat() {
        String h = String.valueOf (p.getDays()*24+p.getHours()+":"); //ha több napig mozogna akkor hozzáadja
        //az eltelt napok számának a 24szeresét az órához
        String s = h + p.toString(new PeriodFormatterBuilder()
                .minimumPrintedDigits(2)
                .printZeroAlways()
                //.appendHours()
                //.appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter());
        return s;
    }

    public String getStepPerMinute()
    {
        Minutes min= p.toStandardMinutes();
        double fullmin = p.getMinutes()+(double)p.getSeconds()/60+p.getHours()*60+p.getDays()*60*24;
        return String.valueOf(Math.round((stepCount/fullmin)*100)/100);
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

}
