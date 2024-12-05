package com.github.nekobanana.dtmcgenerator.config.labelgenerator;

import com.github.nekobanana.dtmcgenerator.sampling.sampler.random.RandomHelperType;

public class ForwardSamplingConfig extends SamplingConfig {
    private RandomHelperType randomMethod;
    private int runs;

    public RandomHelperType getRandomMethod() {
        return randomMethod;
    }

    public void setRandomMethod(RandomHelperType randomMethod) {
        this.randomMethod = randomMethod;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }
}