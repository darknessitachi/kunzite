# Kunzite

Financial application ideas

## Trader

This application which is being developed will provide a means of developing trading strategies that can be optimized
with the Optimizer below.

## Optimizer

Often when one writes trading algorithms a number of input variables are specified such as number of days/ticks to
average.  To generate maximum profit these input variables are tweaked and the algorithm run against historical
data to find the most optimum input variables.  If the algorithm has a large number of input variables the search
space can quickly become too large.  This is where the **Optimizer** comes into play.  It will, using various tactics
attempt to shorten the time taken to find the most optimum set of inputs.

Full writeup can be found in the [wiki](https://github.com/zaradai/kunzite/wiki/Optimizer)

### Performance

Using my i7-3770K @ 3.5GHz with the 'Flood Fill' strategy batching 4000 requests and using 8 evaluation threads
I was able to attain 1.5 MM calculations per second of a simple equation. This strategy ran through all 4 MM possible
calculations in 2768 ms.  In contrast using a 'Hill Climber' strategy I was able to get the correct result in 74 ms
after only 2277 calculations, however it must be noted that this strategy was fortunate to follow the maxima peak.

### Examples

There are integration tests which demonstrate how to setup and execute an optimization on a given evaluation.

### License

Copyright 2014 Zaradai.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.