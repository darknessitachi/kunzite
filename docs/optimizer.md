# Motivation

Often when one writes trading algorithms a number of input variables are specified such as number of days/ticks to average. To generate maximum profit these input variables are tweaked and the algorithm run against historical data to find the most optimum input variables. If the algorithm has a large number of input variables the search space can quickly become extremely large causing the time and server resources needed to search this space infeasible. This is where the Optimizer comes into play. It will, using various tactics attempt to shorten the time taken to find the most optimum set of inputs.

One technique I like suited to algo trading optimizations is called the **Hill Climber**.  A full explanation is provided on [wikipedia](http://en.wikipedia.org/wiki/Hill_climbing).  However one of the flaws of hill climbing is that there may be many plateaus which cause the tactic to end early.  To overcome this shortcoming I have added a shotgun hill climber which basically generates many random start points to spread the chances of hitting the optima peaks.

# Design

![alt text](https://github.com/zaradai/kunzite/raw/master/docs/design.jpg "Optimizer basic architecture")

## Overview

The tactic decides what data points it wishes to search and sends the requests to the Data coordinator.  The coordinator will then attempt to lookup the result in the dataset.  The dataset will return a result if this particular data point has already been calculated otherwise it will return null.  If no result exists the data coordinator schedules an evaluation with the calc engine.  Once the calc engine has returned a result the data coordinator will add to the dataset and return back to the tactic.  The optimizer tactic will use the results to determine if it has found a local maxima, if it has the optimization finishes and returns the result otherwise a new set of search points are generated and sent for computation.

# Optimizer Tactic

The core of the optimization takes place within a Tactic.  The tactic's job is to generate requests to be evaluated, each request will represent one point within the search space.  Once the requests has been evaluated the tactic will use the results to either generate more requests based on its algorithm or choose that it has found the maxima it was looking for.

The tactic logic will be with a class that extends `AbstractTactic`.  There are 3 methods which need to be implemented.

`void initialize()` this method will be called by the optimizer to give the tactics logic chance to initialize and set up up its initial state.

`List<InputRow> prepare()` the implementer will return a list of points within the search space it wishes to get results for.  Once this method returns the requests will be sent immediately to the configured evaluator to be be processed.  Note it is advisable to send requests in batches as it will allow the optimizer to process results whilst other requests are being evaluated.

`boolean process(List<Row> results)` the optimizer will wait until all the results are returned and then call the process method.  The tactics logic will use the results to determine if a new maxima/minima has been found or whether a specific end condition is met.  If it has then returning true will terminate the optimization otherwise `prepare` will be called to get another set of points to search.

## Hill Climber implementation

The implementation is straightforward, first the centre point is taken from the start point provided when the optimization is requested or on subsequent calls it will be the last maxima/minima.  The neighbours of this point is then obtained to seed the next round.

```java
@Override
protected List<InputRow> prepare() {
    // get row to search around
    InputRow centre = (getOptimizedRow() == null) ? getStart() : getOptimizedRow().getInput();
    // generate neighbours for the start condition and send
    return InputRowGenerator.getNeighbours(centre);
}
```

When the results are returned from the evaluator, they are assessed against the previous maxima/minima, if a new point is found the optimization will continue otherwise we are at a peak and therfore found our local maxima/minima.

```java
@Override
protected boolean process(List<Row> results) {
    boolean finished = true;

    for (Row row : results) {
        if (testValue(row)) {
            // new optima found keep going
            finished = false;
        }
    }

    return finished;
}
```

Note `testValue` will check the target value looking for either a minima or maxima as required by the optimization request.

## Input

The system is designed to search an input space which is made up of a number of named variables with each variable having a range of valid values.  The inputs are encapsulated by the `InputRow` which is used to contain each variable and its associated value.  The value is an index to a specific `double` and the range of values is defined by a series.  The `Series` class will generate a number of discrete values within a range.

E.g. I wish to specify that the [SMA](http://www.investopedia.com/terms/s/sma.asp) periods should range from 4 until 40 in steps of 1, I would declare a series using the min-max factory.

```java
Series smaSteps = Series.newMinMaxSeries(4, 40, 1);
```

To enable this definition to be associated with a named variable I create an `InputRowSchema` and using the fluent interface setup the variable. Note the series factory method is called by the fluent builder.

```java
InputRowSchema schema = InputRowSchema.newBuilder()
                .withName("SMA Steps").from(4).until(40).withStep(1)
                .build();
```

The optimizer will now use this schema to run evaluations across the available range until it finds the best SMA step value for the given algo.

# Data Coordinator

todo

# Dataset

todo

# Calc Engine

todo
