Kunzite
=======

Financial application ideas

Optimizer
---------

Often when one writes trading algorithms a number of input variables are specified such as number of days/ticks to
average.  To generate maximum profit these input variables are tweaked and the algorithm run against historical
data to find the most optimum input variables.  If the algorithm has a large number of input variables the search
space can quickly become too large.  This is where the **Optimizer** comes into play.  It will, using various tactics
attempt to shorten the time taken to find the most optimum set of inputs.

### Performance

Using my i7-3770K @ 3.5GHz with the 'Flood Fill' strategy batching 4000 requests and using 8 evaluation threads
I was able to attain 1.5 MM calculations per second of a simple equation. This strategy ran through all 4 MM possible
calculations in 2768 ms.  In contrast using a 'Hill Climber' strategy I was able to get the correct result in 74 ms
after only 2277 calculations