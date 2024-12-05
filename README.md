# dtmc-generator
This tool can generate a dataset with:
* **data**: irreducible DTMCs
* **labels**: time step distribution of multiple Perfect Sampling (or others) executions for each DTMC

### Usage
The tool can operate in two different modes:
- `--dtmcs`: for generating DTMCs (first step).
  The output of this phase is a list of json files named by a progressive number that will be one of the input for the second phase.
- `--labels`: for generating the distributions (second step).
  The output of this phase is a list of json files named in the same way as the input files with information about the generated time step distribution for every DTMC file.

###### First step
In `--dtmcs` mode two arguments must be provided:
- `--config-file ` followed by the path of the configuration file (see [Configuration - first step](#configuration---first-step---dtmcs))
- `--output-dir` followed by the path of the output folder where the DTMC files will be stored

###### Second step
In `--labels` mode three arguments must be provided:
- `--config-file ` followed by the path of the configuration file (see [Configuration - second step](#configuration---second-step---labels))
- `--input-dir` followed by the path of the input folder with the DTMC files generated in the first step
- `--output-dir` followed by the path of the output folder where the DTMC files will be stored

In order to get a help menu with usage description run `dtmc-gen --help`

### Configuration files
The configurations are json files whose structure will be described in the following sections.
Some examples of configuration files are located in the  *./example* folder.
##### Configuration - first step (`--dtmcs`)
The main object has two properties:
```javascript
    {
      "seed": 123456,
      "dtmcGeneratorConfigs": [
        ...
      ]
    }
```
The *seed* property can be null. In that case, a random seed is choosen and an identical configuration file with that seed is generated and saved alongside the original with the suffix "_with_seed.json". This seed ensures that the same configuration files produce the same results.
The property *dtmcGeneratorConfigs* describes groups of DTMCs that need to be generated, and it  is a list of objects with the following structure:
```json
{
      "statesNumberDistribution": {...},
      "edgesNumberDistribution": {...},
      "edgesLocalityDistribution": {...},
      "selfLoopValue": 0.1,
      "connectSCCs": false,
      "numberOfDTMCs": 10
}
```
The property *selfLoopValue*, when present, must be a value in [0, 1). It can be null, in that case random values are chosen for the self loop of every DTMC in that group.
The property *connectSCCs* ensures that an irreducible DTMC is generated on the first try, even if it may not fit all the configuration parameters described in the group. This is done by connetting the SCCs in the case that the generated matrix comes out to be not irreducible. If the property is set to false the generation algorithm is not guaranteed to terminate with every provided configuration.
The property *numberOfDTMCs* is an integer and it is the number of DTMCs that need to be generated with the group configuration.
The properties *statesNumberDistribution*, *edgesNumberDistribution* and *edgesLocalityDistribution* are *distribution* objects and describe the distributions that need to be used to draw values for the number of states, the number of edges for every state, and the locality of the edges, respectively.
The property *statesNumberDistribution* and *edgesNumberDistribution* must yield positive integer values.
The property *edgesLocalityDistribution* must yield integer values and it is used to get the destination state for every edge. For a given state, the value drawn from this distribution is added to the state index to get the destination state for that edge (i.e. state 3 and -2 drawn from *edgesLocalityDistribution* produce ad edge from state 3 to state 1).
Note that interval with of *edgesLocalityDistribution* cannot be greater that minimum number of states drawn from *statesNumberDistribution*.
A *distribution* object can represent a single value or a uniform distribution (or a combination of multiple uniform distributions).
The single value distribution has this structure:
```json
{
        "distributionType": "single_value",
        "n": 4
}
```
The uniform distribution has this structure:
```json
{
        "distributionType": "uniform",
        "min": -4,
        "max": 4
}
```
The manual distribution has this structure:
```json
{
        "distributionType": "manual",
        "distributions": [...],
        "distributionsProbabilities": [...]
}
```
where *distributions* is a list of uniform distributions and *distributionsProbabilities* is a list with the weight of every distribution.
Note: "**single_value**",  "**uniform**" and "**manual**" are the only accepted values for the property *distributionType*.
##### Configuration - second step (`--labels`)
The main object can be one of three different configurations, depending on the algorithm that will be used for generating the time step distribution.
######Perfect Sampling
```json
{
  "samplingMethod": "perfect_sampling",
  "randomMethod": "single_random",
  "statisticalTestConfig": {
     "statisticalTestType": "ZTest",
    "confidence": 0.95,
    "error": 0.01
  }
}
```
The property *randomMethod* indicates the type of coupling for the algorithm. The choice is between "**single_random**" and "**n_random**".
The property *statisticalTestConfig* describes the parameters for a statistical test that is used for determining how many samples need to be produced by Perfect Sampling to obtain the step number distribution. The choice for the value of *statisticalTestType* is between "**studentTTest**" and "**ZTest**".
######Forward Sampling
```json
{
  "samplingMethod": "forward_sampling",
  "randomMethod": "n_random",
  "runs": 1000
}
```
This is a variant of Perfect Sampling where the coupling takes place by going forward instead of backwards in time. It does not produce samples according to the stationary distribution but it can be useful for its time step distribution.
The property *runs* indicate the number of samples that will be extracted for obtain the time step distribution.
######Forward Coupling
```json
{
  "samplingMethod": "forward_coupling"
}
```
This algorithm performs a coupling simulation bewteen every possible couple of states of the DTMC and uses the coupling time to obtain the time step dstribution.

Note: "**perfect_sampling**",  "**forward_sampling**" and "**forward_coupling**" are the only accepted values for the property *samplingMethod*.

### Output
The output of the first step is a group of json files with the following structure:
```json
{
  "dtmc" : [ [ ...], [... ], ...],
  "nextStepSeed" : -6478182413270580960
}
```
The property *dtmc* contains the values of the DTMC transition matrix.
The property *nextStepSeed* is a generated seed and it is used by the second step in order to ensure riproducibility of the experiment.

The output of the second step is a group of json files with the following structure:
```json
{
  "distribution" : {
    "2" : 8,
    "3" : 76,
    "4" : 94,
    "5" : 112,
    ...
  },
  "mean" : 9.925,
  "stdDev" : 6.618699432832872
}
```
The *distribution* property contains a dictionary where every key/value couple in an entry in the histogram that represents the time step distribution. The keys are the time steps (*x* axis) and the values indicate how many times the algorithm has terminated in that number of steps (*y* axis).
The *mean* and *stdDev* properties contains respectively the mean and standard deviation of the distribution.