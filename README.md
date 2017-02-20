### [scala-collections-benchmark](https://l15k4.github.io/scala-collections-benchmark)

Running all benchmarks takes ~ 17 minutes when parallelExecution is disabled :
```
> ;clean;test
```
Results are generated to `target/benchmarks`. In order to publish them to [github pages](https://l15k4.github.io/scala-collections-benchmark), run :
```
$ ./bin/gh-pages-publish.sh
```