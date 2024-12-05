package com.github.nekobanana.dtmcgenerator.config.labelgenerator;

import java.util.Map;

public class OutputSampling {
    private Map<Integer, Long> distribution;
    private double mean;
    private double stdDev;

    public Map<Integer, Long> getDistribution() {
        return distribution;
    }

    public void setDistribution(Map<Integer, Long> distribution) {
        this.distribution = distribution;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }
}
