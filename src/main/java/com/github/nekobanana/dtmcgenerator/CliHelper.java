package com.github.nekobanana.dtmcgenerator;

import org.apache.commons.cli.*;

public class CliHelper {
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

    public static CommandLine parseCLIArgs(String[] args) {
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
        return cmd;
    }
}
