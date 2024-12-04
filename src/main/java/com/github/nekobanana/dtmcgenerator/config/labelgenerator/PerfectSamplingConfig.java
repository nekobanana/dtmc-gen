package com.github.nekobanana.dtmcgenerator.config.labelgenerator;

import com.github.nekobanana.dtmcgenerator.sampling.sampler.random.RandomHelperType;

public class PerfectSamplingConfig extends SamplingConfig {
    private StatisticalTestConfig statisticalTestConfig = new StatisticalTestConfig();
    private RandomHelperType randomMethod;

    public StatisticalTestConfig getStatisticalTestConfig() {
        return statisticalTestConfig;
    }

    public void setStatisticalTestConfig(StatisticalTestConfig statisticalTestConfig) {
        this.statisticalTestConfig = statisticalTestConfig;
    }

    public RandomHelperType getRandomMethod() {
        return randomMethod;
    }

    public void setRandomMethod(RandomHelperType randomMethod) {
        this.randomMethod = randomMethod;
    }
}

