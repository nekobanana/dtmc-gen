package com.github.nekobanana.dtmcgenerator.config.dtmcgenerator;

import com.github.nekobanana.dtmcgenerator.generator.distribution.Distribution;

public class DTMCConfig {
    private Distribution statesNumberDistribution;
    private Distribution edgesNumberDistribution;
    private Distribution edgesLocalityDistribution;
    private Double selfLoopValue;
    private boolean connectSCCs;
    private int numberOfDTMCs;

    public Distribution getStatesNumberDistribution() {
        return statesNumberDistribution;
    }

    public void setStatesNumberDistribution(Distribution statesNumberDistribution) {
        this.statesNumberDistribution = statesNumberDistribution;
    }

    public Distribution getEdgesNumberDistribution() {
        return edgesNumberDistribution;
    }

    public void setEdgesNumberDistribution(Distribution edgesNumberDistribution) {
        this.edgesNumberDistribution = edgesNumberDistribution;
    }

    public Distribution getEdgesLocalityDistribution() {
        return edgesLocalityDistribution;
    }

    public void setEdgesLocalityDistribution(Distribution edgesLocalityDistribution) {
        this.edgesLocalityDistribution = edgesLocalityDistribution;
    }

    public Double getSelfLoopValue() {
        return selfLoopValue;
    }

    public void setSelfLoopValue(Double selfLoopValue) {
        this.selfLoopValue = selfLoopValue;
    }

    public boolean isConnectSCCs() {
        return connectSCCs;
    }

    public void setConnectSCCs(boolean connectSCCs) {
        this.connectSCCs = connectSCCs;
    }

    public int getNumberOfDTMCs() {
        return numberOfDTMCs;
    }

    public void setNumberOfDTMCs(int numberOfDTMCs) {
        this.numberOfDTMCs = numberOfDTMCs;
    }
}
