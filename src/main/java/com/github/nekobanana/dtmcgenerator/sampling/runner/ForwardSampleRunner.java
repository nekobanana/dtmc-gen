package com.github.nekobanana.dtmcgenerator.sampling.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.ForwardSampler;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.RunResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForwardSampleRunner implements SamplerRunner{
    private ForwardSampler sampler;
    private List<RunResult> results = new ArrayList<>();
    private Float avgSteps;
    private Double stdDevSteps;
    private int runs;

    public ForwardSampleRunner(ForwardSampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public void run() {
        avgSteps = null;
        stdDevSteps = null;
        for (int i = 0; i < runs; i++) {
            sampler.reset();
            results.add(sampler.runUntilCoalescence());
        }
    }

    @Override
    public Map<Integer, Long> getStepsDistribution() {return getStepsDistribution(false);}
    public Map<Integer, Long> getStepsDistribution(boolean print) {
        Map<Integer, Long> hist = SamplerRunner.getDistrFromResults(results, RunResult::getSteps);
        if (print) {
            System.out.println("\nPerfect sampling");
            hist.forEach((state, count) ->
                    System.out.println("steps: " + state + ", count: " + count));
        }
        return hist;
    }

    public Float getAvgSteps() {
        avgSteps = (float) results.stream().mapToInt(RunResult::getSteps).sum() / results.size();
        return avgSteps;
    }

    public Double getStdDevSteps() {
        stdDevSteps = Math.sqrt(results.stream()
                .mapToDouble(r -> Math.pow(r.getSteps() - avgSteps, 2)).sum() / (results.size() - 1));
        return stdDevSteps;
    }
    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }
}
