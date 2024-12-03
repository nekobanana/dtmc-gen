package com.github.nekobanana.dtmcgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nekobanana.dtmcgenerator.config.DTMCConfig;
import com.github.nekobanana.dtmcgenerator.config.DTMCGeneratorConfig;
import com.github.nekobanana.dtmcgenerator.config.OutputDTMC;
import com.github.nekobanana.dtmcgenerator.generator.DTMCGenerator;
import com.github.nekobanana.dtmcgenerator.sampling.MethodEnum;
import com.github.nekobanana.dtmcgenerator.utils.RandomUtils;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.la4j.Matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main {
    static Long seed;

    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        Option labels = new Option("l", "labels", false, "generate labels");
        options.addOption(labels);
        Option dtmcs = new Option("d", "dtmcs", false, "generate DTMCs");
        options.addOption(dtmcs);
        Option config = new Option("c", "config-file", true, "input config file");
        config.setRequired(true);
        config.setArgName("file");
        options.addOption(config);
        Option input = new Option("i", "input-dir", true, "input directory containing DTMC files");
        input.setArgName("directory");
        options.addOption(input);
        Option output = new Option("o", "output-dir", true, "output directory for DTMCs with '--dtmcs' flag and for labels with '--labels' flag");
        output.setRequired(true);
        output.setArgName("directory");
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
            boolean hasLabels = cmd.hasOption("labels");
            boolean hasDtmcs = cmd.hasOption("dtmcs");
            if (!hasLabels && !hasDtmcs) {
                throw new ParseException("You must specify either --labels or --dtmcs.");
            }
            if (hasLabels && hasDtmcs) {
                throw new ParseException("--labels and --dtmcs are mutually exclusive.");
            }
            if (hasLabels && !cmd.hasOption("input-dir")) {
                throw new ParseException("--labels requires --input-dir.");
            }
            if (hasDtmcs && cmd.hasOption("input-dir")) {
                throw new ParseException("--input-dir cannot be used with --dtmcs.");
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println();
            printHelp(formatter, options);
            System.exit(1);
        }
        if (!cmd.hasOption("config") && !cmd.hasOption("dtmcs-dir")) {
            throw new ParseException("Missing input config or input DTMCs dir options");
        }
        if (cmd.hasOption("config")) {
            if (!cmd.hasOption("dtmcs-dir")) {
                throw new ParseException("Missing output DTMCs dir option");
            }
            String configFilePath = cmd.getOptionValue("config");
            String outputDirPath = cmd.getOptionValue("dtmcs-dir");
            generateDTMCs(configFilePath, outputDirPath);
        }
        if (cmd.hasOption("dtmcs-dir")) {
            if (!cmd.hasOption("labels-dir")) {
                throw new ParseException("Missing output labels dir option");
            }
            String inputDirPath = cmd.getOptionValue("dtmcs-dir");
            String outputDirPath = cmd.getOptionValue("labels-dir");
            MethodEnum methodEnum = MethodEnum.fromString(cmd.getOptionValue("method"));
            generateLabels(inputDirPath, outputDirPath, methodEnum);
        }

    }

    private static void generateDTMCs(String configFilePath, String outputDirPath) throws IOException {
        DTMCGeneratorConfig generatorConfig = new ObjectMapper().readValue(new File(configFilePath), DTMCGeneratorConfig.class);

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

    private static void generateLabels(String inputDirPath, String outputDirPath, MethodEnum method) throws IOException {
        File dir = new File(inputDirPath);
        File[] directoryListing = dir.listFiles();
        ObjectMapper objectMapper = new ObjectMapper();
        if (directoryListing != null) {
            for (File dtmcFile : directoryListing) {
                OutputDTMC oDtmc = objectMapper.readValue(dtmcFile, OutputDTMC.class);
                RandomUtils.setSeed(oDtmc.getNextStepSeed());
                Matrix P = Matrix.from2DArray(oDtmc.getDtmc());

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
    private static void printHelp(HelpFormatter formatter, Options options) {
        String header = """
                SYNTAX:
                    dtmc-gen [OPTIONS]
                
                REQUIRED OPTIONS:
                    -c, --config-file <file>    Specifies the configuration file.
                    -o, --output-dir <directory>
                                              Specifies the output directory. Required for all executions.

                MUTUALLY EXCLUSIVE OPTIONS:
                    -l, --labels               Generates labels. Requires --input-dir.
                    -d, --dtmcs                Generates DTMCs. Cannot be used with --input-dir.

                OPTIONAL (CONDITIONAL) OPTIONS:
                    -i, --input-dir <directory>
                                              Specifies the input directory containing DTMC files. Required only with --labels.

                EXAMPLES:
                    Generate DTMCs:
                        dtmc-gen --dtmcs --config-file dtmc_config.json --output-dir dtmcs/
                    Generate labels:
                        dtmc-gen --labels --config-file label_config.json --input-dir dtmcs/ --output-dir labels/
                """;
        System.out.println(header);
//        formatter.printHelp(syntax, header, new Options(), "Notes: At least one of --labels or --dtmcs must be specified. --input-dir cannot be used with --dtmcs.");
    }
}