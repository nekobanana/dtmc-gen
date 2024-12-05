package com.github.nekobanana.dtmcgenerator;

import org.apache.commons.cli.*;

public class CliHelper {
    private static void printHelp(HelpFormatter formatter, Options options) {
        String header = """
                Usage:
                    dtmc-gen [OPTIONS]
                
                Operation mode (mutually exclusive, required):
                    -l, --labels               Generates labels. Requires --input-dir.
                    -d, --dtmcs                Generates DTMCs. Cannot be used with --input-dir.

                Required options:
                    -c, --config-file <file>    Specifies the configuration file.
                    -o, --output-dir <directory>
                                              Specifies the output directory. Required for all executions.
                    -i, --input-dir <directory>
                                              Only with --labels flag. Specifies the input directory containing DTMC files.

                Examples:
                    Generate DTMCs:
                        dtmc-gen --dtmcs --config-file example/dtmc_config.json --output-dir example/dtmcs/
                    Generate labels:
                        dtmc-gen --labels --config-file example/label_config.json --input-dir example/dtmcs/ --output-dir example/labels/
                """;
        System.out.println(header);
//        formatter.printHelp(syntax, header, new Options(), "Notes: At least one of --labels or --dtmcs must be specified. --input-dir cannot be used with --dtmcs.");
    }

    public static CommandLine parseCLIArgs(String[] args) {
        Options options = new Options();
        Option labels = new Option("l", "labels", false, "generate labels");
        options.addOption(labels);
        Option dtmcs = new Option("d", "dtmcs", false, "generate DTMCs");
        options.addOption(dtmcs);
        Option config = new Option("c", "config-file", true, "input config file");
        config.setArgName("file");
        options.addOption(config);
        Option input = new Option("i", "input-dir", true, "input directory containing DTMC files");
        input.setArgName("directory");
        options.addOption(input);
        Option output = new Option("o", "output-dir", true, "output directory for DTMCs with '--dtmcs' flag and for labels with '--labels' flag");
        output.setArgName("directory");
        options.addOption(output);
        Option help = new Option("h", "help", false, "generate labels");
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                System.out.println("Help menu:");
                System.out.println();
                printHelp(formatter, options);
                System.exit(1);
            }
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
            if (!cmd.hasOption("config-file") || !cmd.hasOption("output-dir")) {
                throw new ParseException("Missing --config-file e/or --output-dir.");
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println();
            printHelp(formatter, options);
            System.exit(1);
        }
        return cmd;
    }
}
