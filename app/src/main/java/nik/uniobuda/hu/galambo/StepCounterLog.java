package nik.uniobuda.hu.galambo;

import java.io.Serializable;

/**
 * Created by Adam on 2017. 05. 05..
 */

public class StepCounterLog implements Serializable {
    int stepCount;
    double minutes;

    public StepCounterLog(int stepCount, double minutes) {
        this.stepCount = stepCount;
        this.minutes = minutes;
    }

    public int getStepCount() {

        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }
}
