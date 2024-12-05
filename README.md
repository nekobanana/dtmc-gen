# dtmc-generator
This tool can generate a dataset with:
* **data**: DTMCs
* **labels**: time step distribution of multiple Perfect Sampling (or others) executions for each DTMC

#### Usage
The tool can operate in two different modes:
- `--dtmcs`: for generating DTMCs (first step)
- `--labels`: for generating the distributions (second step)

###### First step
In `--dtmcs` mode two arguments must be provided:
- `--config-file ` followed by the path of the configuration file (see [Configuration - first step](#configuration---first-step---dtmcs))
- `--output-dir` followed by the path of the output folder where the DTMC files will be stored

###### Second step
In `--labels` mode three arguments must be provided:
- `--config-file ` followed by the path of the configuration file (more on this later)
- `--input-dir` followed by the path of the input folder with the DTMC files generated in the first step
- `--output-dir` followed by the path of the output folder where the DTMC files will be stored

In order to get a help menu with usage description run `dtmc-gen --help`

#### Configuration files
The configurations are json files with the following structures.
###### Configuration - first step (`--dtmcs`)

