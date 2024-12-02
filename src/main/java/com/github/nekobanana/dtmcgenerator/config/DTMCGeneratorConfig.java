package com.github.nekobanana.dtmcgenerator.config;

import java.util.ArrayList;
import java.util.List;

public class DTMCGeneratorConfig {

    private Long seed;
    private List<DTMCConfig> dtmcGeneratorConfigs = new ArrayList<>();

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public List<DTMCConfig> getDtmcGeneratorConfigs() {
        return dtmcGeneratorConfigs;
    }

    public void setDtmcGeneratorConfigs(List<DTMCConfig> dtmcGeneratorConfigs) {
        this.dtmcGeneratorConfigs = dtmcGeneratorConfigs;
    }
}
