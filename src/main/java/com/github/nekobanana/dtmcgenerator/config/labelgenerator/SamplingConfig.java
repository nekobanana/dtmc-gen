package com.github.nekobanana.dtmcgenerator.config.labelgenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.nekobanana.dtmcgenerator.sampling.SamplingMethod;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "samplingMethod", visible = true) @JsonSubTypes({
        @JsonSubTypes.Type(value = PerfectSamplingConfig.class, name = SamplingMethod.PERFECT_SAMPLING),
        @JsonSubTypes.Type(value = ForwardSamplingConfig.class, name = SamplingMethod.FORWARD_SAMPLING),
        @JsonSubTypes.Type(value = ForwardCouplingConfig.class, name = SamplingMethod.FORWARD_COUPLING)
})
public abstract class SamplingConfig {
    private String samplingMethod;

    public String getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(String samplingMethod) {
        this.samplingMethod = samplingMethod;
    }
}
