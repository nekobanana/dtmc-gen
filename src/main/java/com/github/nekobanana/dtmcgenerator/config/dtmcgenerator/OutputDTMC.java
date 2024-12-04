package com.github.nekobanana.dtmcgenerator.config.dtmcgenerator;

public class OutputDTMC {
    double[][] dtmc;
    long nextStepSeed;

    public double[][] getDtmc() {
        return dtmc;
    }

    public void setDtmc(double[][] dtmc) {
        this.dtmc = dtmc;
    }


    public long getNextStepSeed() {
        return nextStepSeed;
    }

    public void setNextStepSeed(long nextStepSeed) {
        this.nextStepSeed = nextStepSeed;
    }
}
