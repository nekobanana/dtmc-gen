package com.github.nekobanana.dtmcgenerator.sampling.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.random.RandomHelperType;

import java.util.Arrays;

public enum TestType {

    STUDENT_T_TEST("studentTTest"),
    Z_TEST("ZTest"),
    MODIFIED_Z_TEST("modified_ZTest");

    private final String name;

    TestType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static TestType fromName(String name) {
        return Arrays.stream(TestType.values())
                .filter(v -> v.getName().equals(name)).findFirst()
                .orElseThrow();
    }
}
