package com.github.nekobanana.dtmcgenerator.config.labelgenerator;

import com.github.nekobanana.dtmcgenerator.sampling.test.StatisticalTest;
import com.github.nekobanana.dtmcgenerator.sampling.test.TestType;

public class StatisticalTestConfig {
    private double confidence;
    private double error;
    private TestType statisticalTestType;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public TestType getStatisticalTestType() {
        return statisticalTestType;
    }

    public void setStatisticalTestType(TestType statisticalTestType) {
        this.statisticalTestType = statisticalTestType;
    }
}
