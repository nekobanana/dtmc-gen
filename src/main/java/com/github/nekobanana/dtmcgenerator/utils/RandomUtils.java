package com.github.nekobanana.dtmcgenerator.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    public static int getValueFromDistribution(List<Double> distribution) {
        double leftThreshold;
        double rightThreshold = 0;
        double r = rand.nextFloat();
        for (int j = 0; j < distribution.size(); j++) {
            leftThreshold = rightThreshold;
            rightThreshold = distribution.get(j) + leftThreshold;
            if (r < rightThreshold) {
                return j;
            }
        }
        throw new RuntimeException("Error generating random number");
    }

    public static final Random rand = new Random();
    public static void setSeed(long seed) {
        rand.setSeed(seed);
    }

}
