package com.github.nekobanana.dtmcgenerator.sampling.sampler.random;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Random;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY, property = "randomMethod") @JsonSubTypes({
//        @JsonSubTypes.Type(value = SingleRandomHelper.class, name = RandomHelperType.SINGLE_RANDOM),
//        @JsonSubTypes.Type(value = NRandomHelper.class, name = RandomHelperType.N_RANDOM),
//})
public abstract class RandomHelper {

    protected Random rand;
    protected int n;

    public RandomHelper(Random rand, int n) {
        this.rand = rand;
        this.n = n;
    }

    public abstract double getRandomDouble();
    public abstract int getRandomInt();
    public abstract void init();

    public static RandomHelper getRandomHelper(RandomHelperType helperType, Random rand, int n) {
        RandomHelper rHelper = switch (helperType) {
            case SINGLE_RANDOM -> new SingleRandomHelper(rand, n);
            case N_RANDOM -> new NRandomHelper(rand, n);
        };
        return rHelper;
    }
}
