package com.github.nekobanana.dtmcgenerator.sampling.sampler.random;

import java.util.Random;

public class NRandomHelper extends RandomHelper {

    public NRandomHelper(Random rand, int n) {
        super(rand, n);
    }

    @Override
    public double getRandomDouble() {
        return rand.nextDouble();
    }

    @Override
    public int getRandomInt() {
        return rand.nextInt(n);
    }

    @Override
    public void init() {}
}
