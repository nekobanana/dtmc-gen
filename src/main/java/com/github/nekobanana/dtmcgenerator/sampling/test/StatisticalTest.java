package com.github.nekobanana.dtmcgenerator.sampling.test;

import org.apache.commons.lang3.NotImplementedException;

public abstract class StatisticalTest {
    protected String name;
    protected double confidence;
    protected double maxError;

    protected int currentSamplesSize = 0;
    protected double currentSum = 0.;
    protected double currentVar = 0.;
    protected double currentError;


    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }

    public double getCurrentError() {
        return currentError;
    }

    protected double getCriticalValue() {
        throw new NotImplementedException();
    }

    public void addNewSample(double newSample) {
        currentSamplesSize++;
        double previousSum = currentSum;
        currentSum += newSample;
        double newMean = currentSum / currentSamplesSize;
        if (currentSamplesSize > 1) {
            double previousMean = previousSum / (currentSamplesSize - 1);
            currentVar = ((currentSamplesSize - 2) * currentVar + (newSample - previousMean) * (newSample - newMean)) / (currentSamplesSize - 1);
        } else {
            currentVar = 0.0;
        }
    }

    public double getMean() {
        return currentSum / currentSamplesSize;
    }

    public double getStdDev() {
        return Math.sqrt(currentVar);
    }

    public boolean test() {
        double standardError = getStdDev() / Math.sqrt(currentSamplesSize);
        double value = getCriticalValue();
        double marginOfError = value * standardError;
        currentError = marginOfError / getMean();
        return currentError <= maxError;
    }

    @Override
    public String toString() {
        return name + "{" +
                "samplesSize=" + currentSamplesSize +
                ", samplesMean=" + getMean() +
                ", samplesStdDev=" + getStdDev() +
                ", confidence=" + confidence +
                ", maxError=" + maxError +
                ", currentError=" + currentError +
                '}';
    }

    public int getSamplesSize() {
        return currentSamplesSize;
    }
    public void setSamplesSize(int samplesSize) {currentSamplesSize = samplesSize;}
    public String getName() {
        return name;
    }

    public double getConfidence() {
        return confidence;
    }

    public double getMaxError() {
        return maxError;
    }


    public static StatisticalTest getStatisticalTest(TestType testType) {
        return switch (testType) {
            case STUDENT_T_TEST -> new StudentTTest();
            case Z_TEST -> new ZTest();
            case MODIFIED_Z_TEST -> new ModifiedZTest();
        };
    }
}
