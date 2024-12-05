package com.github.nekobanana.dtmcgenerator.sampling.runner;

import com.github.nekobanana.dtmcgenerator.sampling.sampler.ForwardCoupler;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.RunResult;
import org.apache.commons.math3.util.Combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ForwardCouplingRunner implements SamplerRunner {
    private ForwardCoupler coupler;
    private List<RunResult> results = new ArrayList<>();
    private Double avgSteps;
    private Double stdDevSteps;

    private static final String postprocDirPath = "postprocess/";
    private static final String outputDirPath = postprocDirPath + "results/";

    public ForwardCouplingRunner(ForwardCoupler coupler) {
        this.coupler = coupler;
    }

    @Override
    public void run() {
        avgSteps = null;
        stdDevSteps = null;
        Combinations couples = new Combinations(coupler.getN(), 2);
        for (int[] pair : couples) {
            coupler.reset();
            results.add(coupler.runUntilCoalescence(pair[0], pair[1]));
        }
    }

    public Map<Integer, Double> getStatesDistribution() {return getStatesDistribution(false);}
    public Map<Integer, Double> getStatesDistribution(boolean print) {
        Map<Integer, Double> pi = SamplerRunner.getDistrFromResults(results, RunResult::getSampledState)
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e ->  (double)e.getValue() / results.size()));
        if (print) {
            System.out.println("\nPerfect sampling");
            pi.forEach((state, count) ->
                    System.out.println("state " + state + ": " + count));
        }
        return pi;
    }

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

    public int getAvgStepsPlusStdDev(double sigmaCount) {
        return (int) Math.round(getAvgSteps() + sigmaCount * getStdDevSteps());
    }

    @Override
    public Double getAvgSteps() {
            avgSteps = results.stream().mapToDouble(RunResult::getSteps).sum() / results.size();
        return avgSteps;
    }

    @Override
    public Double getStdDevSteps() {
            stdDevSteps = Math.sqrt(results.stream()
                    .mapToDouble(r -> Math.pow(r.getSteps() - avgSteps, 2)).sum() / (results.size() - 1));
        return stdDevSteps;
    }

    public double[] getResultsSteps() {
        return results.stream().mapToDouble(r -> r.getSteps()).toArray();
    }

    public int getNRuns() {
        return results.size();
    }
}
