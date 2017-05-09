package nik.uniobuda.hu.galambo;

import java.io.Serializable;

/**
 * Created by Adam on 2017. 05. 05..
 */

public class StepCounterLog implements Serializable {
    int stepCount;
    int minutes;
    int hours;
    int seconds;

    public StepCounterLog(int stepCount, int hours, int minutes,int seconds) {
        this.stepCount = stepCount;
        this.minutes = minutes;
        this.hours = hours;
        this.seconds = seconds;
    }

    public int getStepCount() {

        return stepCount;
    }

    public String getTimeInFormat()
    {

        return ((hours)>9?String.valueOf(hours):"0"+String.valueOf(hours))+":"
                +((minutes)>9?String.valueOf(minutes):"0"+String.valueOf(minutes))+":"
                +((seconds)>9?String.valueOf(seconds):"0"+String.valueOf(seconds));
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getMinutes() {
        return minutes;
    }
}
