package com.github.nekobanana.dtmcgenerator.sampling.sampler;

import com.github.nekobanana.dtmcgenerator.utils.RandomUtils;
import org.la4j.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForwardCoupler extends Sampler {
    int currentTime = 0;

    public ForwardCoupler(Matrix P) {
        super(P);
    }

    @Override
    public void reset() {
        currentTime = 0;
    }

    public RunResult runUntilCoalescence(int startState1, int startState2) {
//        for (int[] pair: new Combinations(n, 2)) {
//        int currentState1 = pair[0];
//        int currentState2 = pair[1];
        int currentState1 = startState1;
        int currentState2 = startState2;
        while (currentState1 != currentState2) {
            currentTime++;
            List<Double> minProbabilities = Arrays.asList(new Double[n]);
            for (int i = 0; i < n; i++) {
                minProbabilities.set(i, Math.min(P.get(currentState1, i), P.get(currentState2, i)));
            }
            double random = rand.nextDouble();
            double minProbabilitiesSum = minProbabilities.stream().mapToDouble(p -> p).sum();
            if (random < minProbabilitiesSum) {
                int commonState = RandomUtils.getValueFromDistribution(minProbabilities.stream()
                        .map(p -> p / minProbabilitiesSum).collect(Collectors.toList()));
                currentState1 = commonState;
                currentState2 = commonState;
            } else {
                List<Double> residualProb1 = getResidualProb(currentState1, minProbabilities);
                List<Double> residualProb2 = getResidualProb(currentState2, minProbabilities);
                currentState1 = RandomUtils.getValueFromDistribution(residualProb1);
                currentState2 = RandomUtils.getValueFromDistribution(residualProb2);
            }
        }
//        }
        return new RunResult(currentState1, currentTime);
    }

    private List<Double> getResidualProb(int currentState, List<Double> minProbabilities) {
        List<Double> state1Row = Arrays.stream(P.getRow(currentState).toDenseVector().toArray()).boxed().collect(Collectors.toList());
        List<Double> residualProb1 = IntStream.range(0, n).mapToDouble(i -> state1Row.get(i) - minProbabilities.get(i)).boxed().collect(Collectors.toList());
        double residualProbSum = residualProb1.stream().mapToDouble(p -> p).sum();
        return residualProb1.stream().map(p -> p / residualProbSum).collect(Collectors.toList());
    }


    protected int generateNextStateNumberFromRandomValue(int i, double random) {
        double leftThreshold;
        double rightThreshold = 0;
        for (int j = 0; j < n; j++) {
            leftThreshold = rightThreshold;
            rightThreshold = P.get(i, j) + leftThreshold;
            if (random < rightThreshold) {
                return j;
            }
        }
        try {
            return RandomUtils.getValueFromDistribution(
                    Arrays.stream(P.getRow(i).toDenseVector().toArray()).boxed().collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException("Error generating next state number");
        }
    }
}
