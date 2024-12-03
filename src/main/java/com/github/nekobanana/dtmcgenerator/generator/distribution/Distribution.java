package com.github.nekobanana.dtmcgenerator.generator.distribution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonDeserialize(using = DistributionDeserializer.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "distributionType") @JsonSubTypes({
        @JsonSubTypes.Type(value = SingleValueDistribution.class, name = DistributionType.SINGLE_VALUE),
        @JsonSubTypes.Type(value = UniformDistribution.class, name = DistributionType.UNIFORM),
        @JsonSubTypes.Type(value = ManualDistribution.class, name = DistributionType.MANUAL)
})
public interface Distribution {
    void setSeed(long seed);
    @JsonIgnore
    int getSample();
    @JsonIgnore
    int getMin();
    @JsonIgnore
    int getMax();
    @JsonIgnore
    default int getIntervalLength() {
        return getMax() - getMin() + 1;
    }

}
