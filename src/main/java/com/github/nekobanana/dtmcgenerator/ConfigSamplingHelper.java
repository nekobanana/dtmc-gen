package com.github.nekobanana.dtmcgenerator;

import com.github.nekobanana.dtmcgenerator.config.labelgenerator.ForwardSamplingConfig;
import com.github.nekobanana.dtmcgenerator.config.labelgenerator.PerfectSamplingConfig;
import com.github.nekobanana.dtmcgenerator.config.labelgenerator.SamplingConfig;
import com.github.nekobanana.dtmcgenerator.sampling.SamplingMethod;
import com.github.nekobanana.dtmcgenerator.sampling.runner.ForwardCouplingRunner;
import com.github.nekobanana.dtmcgenerator.sampling.runner.ForwardSampleRunner;
import com.github.nekobanana.dtmcgenerator.sampling.runner.PerfectSampleRunner;
import com.github.nekobanana.dtmcgenerator.sampling.runner.SamplerRunner;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.ForwardCoupler;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.ForwardSampler;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.PerfectSampler;
import com.github.nekobanana.dtmcgenerator.sampling.sampler.random.RandomHelperType;
import com.github.nekobanana.dtmcgenerator.sampling.test.StatisticalTest;
import org.la4j.Matrix;

import java.lang.reflect.InvocationTargetException;

public class ConfigSamplingHelper {

    public static SamplerRunner getSamplerRunner(SamplingConfig config, Matrix P) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return switch (config.getSamplingMethod()) {
            case SamplingMethod.PERFECT_SAMPLING -> {
                PerfectSamplingConfig psConfig = (PerfectSamplingConfig) config;
                PerfectSampler samplerCFTP = new PerfectSampler(P, psConfig.getRandomMethod());
                StatisticalTest statTest = StatisticalTest.getStatisticalTest(psConfig.getStatisticalTestConfig().getStatisticalTestType());
                statTest.setConfidence(psConfig.getStatisticalTestConfig().getConfidence());
                statTest.setMaxError(psConfig.getStatisticalTestConfig().getError());
                PerfectSampleRunner perfectSampleRunner = new PerfectSampleRunner(samplerCFTP, statTest);
                yield perfectSampleRunner;
            }
            case SamplingMethod.FORWARD_SAMPLING -> {
                ForwardSamplingConfig fsConfig = (ForwardSamplingConfig) config;
                ForwardSampler forwardSampler = new ForwardSampler(P, fsConfig.getRandomMethod());
                ForwardSampleRunner forwardCouplingRunner = (new ForwardSampleRunner(forwardSampler));
                forwardCouplingRunner.setRuns(fsConfig.getRuns());
                yield forwardCouplingRunner;
            }
            case SamplingMethod.FORWARD_COUPLING -> {
                ForwardCoupler forwardCoupler = new ForwardCoupler(P);
                ForwardCouplingRunner forwardCouplingRunner = (new ForwardCouplingRunner(forwardCoupler));
                yield forwardCouplingRunner;
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + config.getSamplingMethod());
        };
    }
}
