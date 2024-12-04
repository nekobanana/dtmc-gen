package com.github.nekobanana.dtmcgenerator.sampling.sampler.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RandomHelperType {
    SINGLE_RANDOM("single_random"),
    N_RANDOM("n_random");

    private final String name;
    RandomHelperType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static RandomHelperType fromName(String name) {
        return Arrays.stream(RandomHelperType.values())
                .filter(v -> v.getName().equals(name)).findFirst()
                .orElseThrow();
    }

}
