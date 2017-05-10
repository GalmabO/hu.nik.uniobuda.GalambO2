package nik.uniobuda.hu.galambo;

import org.joda.time.Duration;
import org.joda.time.MonthDay;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodPrinter;

import java.io.Serializable;

/**
 * Created by Adam on 2017. 05. 05..
 */

public class StepCounterLog implements Serializable {
    int stepCount;
    Period p;

    //Gsonnak??
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
        String s = p.toString(new PeriodFormatterBuilder()
                .minimumPrintedDigits(2)
                .printZeroAlways()
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter());
        return s;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

}
