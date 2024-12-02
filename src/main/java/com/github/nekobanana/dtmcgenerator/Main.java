package com.github.nekobanana.dtmcgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nekobanana.dtmcgenerator.config.DTMCConfig;
import com.github.nekobanana.dtmcgenerator.config.DTMCGeneratorConfig;
import com.github.nekobanana.dtmcgenerator.generator.DTMCGenerator;
import com.github.nekobanana.dtmcgenerator.utils.RandomUtils;
import org.apache.commons.cli.*;
import org.la4j.Matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();

        Option config = new Option("c", "config", true, "input config file");
        config.setRequired(true);
        options.addOption(config);

        Option output = new Option("o", "output", true, "output folder (if not provided the program will use this folder)");
        output.setRequired(false);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
//        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = parser.parse(options, args);
        String configFilePath = cmd.getOptionValue("config");
        String outputDirPath = cmd.getOptionValue("output");
        DTMCGeneratorConfig generatorConfig = new ObjectMapper().readValue(new File(configFilePath), DTMCGeneratorConfig.class);

        long seed;
        if (generatorConfig.getSeed() != null) {
            seed = generatorConfig.getSeed();
        }
        else {
            seed = new Random().nextInt();
            generatorConfig.setSeed(seed);
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
                writeMatrixToFile(P, Paths.get(outputDirPath, matCounter++ + ".json"));
            }
        }
    }

    public static void writeMatrixToFile(Matrix P, Path filePath) throws IOException {
        double[][] matrix = matrixToArray(P);
        BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(filePath)));
        writer.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(matrix));
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