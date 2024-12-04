package com.github.nekobanana.dtmcgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nekobanana.dtmcgenerator.config.dtmcgenerator.DTMCConfig;
import com.github.nekobanana.dtmcgenerator.config.dtmcgenerator.DTMCGeneratorConfig;
import com.github.nekobanana.dtmcgenerator.config.dtmcgenerator.OutputDTMC;
import com.github.nekobanana.dtmcgenerator.config.labelgenerator.SamplingConfig;
import com.github.nekobanana.dtmcgenerator.generator.DTMCGenerator;
import com.github.nekobanana.dtmcgenerator.sampling.runner.SamplerRunner;
import com.github.nekobanana.dtmcgenerator.utils.RandomUtils;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.la4j.Matrix;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.Random;

public class Main {
    static Long seed;

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CommandLine cmd = CliHelper.parseCLIArgs(args);

        if (cmd.hasOption("dtmcs")) {
            String configFilePath = cmd.getOptionValue("c");
            String outputDirPath = cmd.getOptionValue("o");
            generateDTMCs(configFilePath, outputDirPath);
        }
        else if (cmd.hasOption("labels")) {
            String configFilePath = cmd.getOptionValue("c");
            String inputDirPath = cmd.getOptionValue("i");
            String outputDirPath = cmd.getOptionValue("o");
            generateLabels(configFilePath, inputDirPath, outputDirPath);
        }
    }

    private static void generateDTMCs(String configFilePath, String outputDirPath) throws IOException {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            throw new FileNotFoundException("Can't find file " + configFilePath);
        }
        DTMCGeneratorConfig generatorConfig = new ObjectMapper().readValue(configFile, DTMCGeneratorConfig.class);

        if (generatorConfig.getSeed() != null) {
            seed = generatorConfig.getSeed();
        }
        else {
            seed = new Random().nextLong();
            generatorConfig.setSeed(seed);
            String configWithSeedFileName = FilenameUtils.getPath(configFilePath) +
                    FilenameUtils.getBaseName(configFilePath) + "_with_seed." +
                    FilenameUtils.getExtension(configFilePath);
            writeObjectToFile(generatorConfig, configWithSeedFileName);
        }
        System.out.println("Seed: " + seed);
        RandomUtils.setSeed(seed);

        int matCounter = 0;
        for (DTMCConfig dtmcConfig: generatorConfig.getDtmcGeneratorConfigs()) {
            int N = dtmcConfig.getStates();
            System.out.println("Generating matrix...");
            DTMCGenerator dtmcGenerator = new DTMCGenerator();
            dtmcGenerator.setEdgesNumberDistribution(dtmcConfig.getEdgesNumberDistribution());
            dtmcGenerator.setEdgesLocalityDistribution(dtmcConfig.getEdgesLocalityDistribution());
            dtmcGenerator.setN(N);
            for (int i = 0; i < dtmcConfig.getNumberOfDTMCs(); i++) {
                Matrix P = dtmcGenerator.getMatrix();
                writeDTMCToFile(P, Paths.get(outputDirPath, matCounter++ + ".json").toString());
            }
        }
    }

    private static void generateLabels(String configFilePath, String inputDirPath, String outputDirPath) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            throw new FileNotFoundException("Can't find file " + configFilePath);
        }

        SamplingConfig samplingConfig = new ObjectMapper().readValue(configFile, SamplingConfig.class);
        File dir = new File(inputDirPath);
        File[] directoryListing = dir.listFiles();
        ObjectMapper objectMapper = new ObjectMapper();
        if (directoryListing != null) {
            for (File dtmcFile : directoryListing) {
                OutputDTMC oDtmc = objectMapper.readValue(dtmcFile, OutputDTMC.class);
                RandomUtils.setSeed(oDtmc.getNextStepSeed());
                Matrix P = Matrix.from2DArray(oDtmc.getDtmc());
                SamplerRunner runner = ConfigSamplingHelper.getSamplerRunner(samplingConfig, P);
                runner.run();
                writeObjectToFile(runner.getStepsDistribution(), Paths.get(outputDirPath, dtmcFile.getName()).toString());
            }
        } else {
            throw new NotDirectoryException(inputDirPath + " is not a directory");
        }
    }

    public static void writeDTMCToFile(Matrix P, String filePath) throws IOException {
        double[][] matrix = matrixToArray(P);
        OutputDTMC outputDTMC = new OutputDTMC();
        outputDTMC.setDtmc(matrix);
        outputDTMC.setNextStepSeed(RandomUtils.rand.nextLong());
        writeObjectToFile(outputDTMC, filePath);
    }

    public static void writeObjectToFile(Object object, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object));
        writer.close();
    }
    
    public static double[][] matrixToArray(Matrix matrix) {
        double[][] result = new double[matrix.rows()][matrix.columns()];
        for (int i = 0; i < matrix.rows(); i++) {
            result[i] = matrix.getRow(i).toDenseVector().toArray();
        }
        return result;
    }

}