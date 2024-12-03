package com.github.nekobanana.dtmcgenerator.sampling.test;

public class ModifiedZTest extends ZTest {

    public ModifiedZTest() {
        name = "ModifiedZTest";
    }

    @Override
    public boolean test() {
        return super.test() && currentSamplesSize > getStdDev() * 2;
    }

}
