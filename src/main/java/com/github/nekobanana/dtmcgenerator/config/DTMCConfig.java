package com.github.nekobanana.dtmcgenerator.config;

import com.github.nekobanana.dtmcgenerator.generator.distribution.Distribution;

public class DTMCConfig {
    private int states;
    private Distribution edgesNumberDistribution;
    private Distribution edgesLocalityDistribution;
    private Double selfLoopValue;
    private boolean connectSCCs;
    private int numberOfDTMCs;

    public int getStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
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
