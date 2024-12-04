package com.github.nekobanana.dtmcgenerator.sampling.runner;

import com.github.nekobanana.dtmcgenerator.sampling.sampler.DumbSampler;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DumbSampleRunner implements SamplerRunner {
    private DumbSampler sampler;
    private int steps = 1;
    private List<Integer> results = new ArrayList<>();
    private int runs;

    public DumbSampleRunner(DumbSampler sampler) {
        this.sampler = sampler;
    }
    public DumbSampleRunner steps(int steps) {
        this.steps = steps;
        return this;
    }

    @Override
    public void run() {
        int r;
        int initialState = 0;
        for (int i = 0; i < runs; i++) {
            sampler.reset();
            initialState = (initialState + 1) % sampler.getN();
            r = sampler.runForNSteps(initialState, steps);
            results.add(r);
        }
    }

    @Override
    public Map<Integer, Long> getStepsDistribution() {
        throw new NotImplementedException();
    }

    public Map<Integer, Double> getStatesDistribution() {return this.getStatesDistribution(false);}
    public Map<Integer, Double> getStatesDistribution(boolean print) {
        Map<Integer, Double> pi = SamplerRunner.getDistrFromResults(results, Function.identity())
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (double)e.getValue() / results.size()));
        if (print) {
            System.out.println("\nDumb sampling");
            System.out.println("Running for " + steps + " steps");
            pi.forEach((state, count) -> System.out.println("state " + state + ": " + count));
        }
        return pi;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getRuns() {
        return runs;
    }
}
