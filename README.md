# java-experiments

This repository contains my some benchmarks for Java.

# How to 

## Build the benchmark ##

``` shell
mvn clean
mvn install
```

## Execute benchmarks ##

**Run all benchmarks**

``` shell
java -jar target/experiments-0.2.0.jar
```

**Run benchmarks that match a given pattern**

``` shell
java -jar target/experiments-0.2.0.jar MapDiff
```

## Performance benchmark results ##

**macOS**

``` shell
Benchmark                                           Mode  Cnt          Score          Error  Units
EnumSetAndMap.enumSet                              thrpt   10  353748827.833 ± 17104002.705  ops/s
EnumSetAndMap.enumSetString                        thrpt   10  337556061.859 ± 21481383.234  ops/s
EnumSetAndMap.hashSet                              thrpt   10  149819307.535 ±  6777848.760  ops/s
EnumSetAndMap.hashSetString                        thrpt   10  194440260.629 ±  4835322.122  ops/s
FiveStringConcatenation.testGuavaJoiner            thrpt   10    6375682.379 ±   269876.729  ops/s
FiveStringConcatenation.testPlus                   thrpt   10   24636347.739 ±  3143669.857  ops/s
FiveStringConcatenation.testStreamJoining          thrpt   10    6359952.503 ±   616017.846  ops/s
FiveStringConcatenation.testStringBuffer           thrpt   10   33650594.450 ±  2468801.949  ops/s
FiveStringConcatenation.testStringBuilder          thrpt   10   33145511.668 ±  2200888.219  ops/s
FiveStringConcatenation.testStringConcat           thrpt   10   16573001.949 ±  1125882.816  ops/s
FiveStringConcatenation.testStringFormat           thrpt   10    1160504.181 ±    82287.019  ops/s
FiveStringConcatenation.testStringJoin             thrpt   10   11506679.590 ±   818026.405  ops/s
FiveStringConcatenation.testStringUtilsJoin        thrpt   10    9966039.148 ±   778868.028  ops/s
LoggingPerf.debugCreateTemporaryObjectUsingFormat  thrpt   10    1050913.795 ±    53644.489  ops/s
LoggingPerf.debugCreateTemporaryObjectUsingPlus    thrpt   10    4541870.750 ±   292289.149  ops/s
LoggingPerf.debugStandardSyntax                    thrpt   10  495655947.333 ± 36600772.089  ops/s
LoggingPerf.debugStandardSyntaxWithIfGuard         thrpt   10  497598108.268 ± 43350670.586  ops/s
LoggingPerf.infoCreateTemporaryObjectUsingFormat   thrpt   10     134139.655 ±    11276.097  ops/s
LoggingPerf.infoCreateTemporaryObjectUsingPlus     thrpt   10     137492.232 ±    10643.037  ops/s
LoggingPerf.infoStandardSyntax                     thrpt   10     141806.298 ±    12904.133  ops/s
LoggingPerf.infoStandardSyntaxWithIfGuard          thrpt   10     138354.224 ±     7822.000  ops/s
MapDiff.simpleAlgorithmIterator                    thrpt   10     305126.467 ±    17732.474  ops/s
MapDiff.simpleAlgorithmUsingKeySet                 thrpt   10     307091.215 ±    14757.370  ops/s
MapDiff.simpleAlgorithmUsingMapEntry               thrpt   10     316894.328 ±    21180.479  ops/s
MapDiff.streamBasedAlgorithm                       thrpt   10     286784.477 ±    26800.036  ops/s
TenStringConcatenation.testGuavaJoiner             thrpt   10    3717354.300 ±   233268.708  ops/s
TenStringConcatenation.testPlus                    thrpt   10   15083265.639 ±   653669.878  ops/s
TenStringConcatenation.testStreamJoining           thrpt   10    3862358.477 ±   297029.355  ops/s
TenStringConcatenation.testStringBuffer            thrpt   10   19307196.970 ±   781697.122  ops/s
TenStringConcatenation.testStringBuilder           thrpt   10   18809702.598 ±   847692.401  ops/s
TenStringConcatenation.testStringConcat            thrpt   10    6806453.822 ±   475703.744  ops/s
TenStringConcatenation.testStringFormat            thrpt   10     598607.615 ±    25438.153  ops/s
TenStringConcatenation.testStringJoin              thrpt   10    5458526.297 ±   360210.385  ops/s
TenStringConcatenation.testStringUtilsJoin         thrpt   10    6208896.398 ±   307399.204  ops/s
ThreeStringConcatenation.testGuavaJoiner           thrpt   10    9695640.906 ±   383917.513  ops/s
ThreeStringConcatenation.testPlus                  thrpt   10   42066011.884 ±  2610308.960  ops/s
ThreeStringConcatenation.testStreamJoining         thrpt   10    9258926.039 ±   901488.451  ops/s
ThreeStringConcatenation.testStringBuffer          thrpt   10   49397567.200 ±  2641019.241  ops/s
ThreeStringConcatenation.testStringBuilder         thrpt   10   48607461.072 ±  2648763.304  ops/s
ThreeStringConcatenation.testStringConcat          thrpt   10   31323286.353 ±  2674772.234  ops/s
ThreeStringConcatenation.testStringFormat          thrpt   10    1662917.162 ±    97004.545  ops/s
ThreeStringConcatenation.testStringJoin            thrpt   10   14045993.876 ±   913426.601  ops/s
ThreeStringConcatenation.testStringUtilsJoin       thrpt   10   14299423.579 ±   589616.016  ops/s
TwoStringConcatenation.testGuavaJoiner             thrpt   10   14558473.930 ±   793282.325  ops/s
TwoStringConcatenation.testPlus                    thrpt   10   55603866.861 ±  8282732.633  ops/s
TwoStringConcatenation.testStreamJoining           thrpt   10   13492624.078 ±   850620.541  ops/s
TwoStringConcatenation.testStringBuffer            thrpt   10   70909706.553 ±  4195239.161  ops/s
TwoStringConcatenation.testStringBuilder           thrpt   10   68083751.811 ±  3475253.049  ops/s
TwoStringConcatenation.testStringConcat            thrpt   10   56202236.653 ±  3385778.575  ops/s
TwoStringConcatenation.testStringFormat            thrpt   10    2217324.019 ±   118414.409  ops/s
TwoStringConcatenation.testStringJoin              thrpt   10   22285774.521 ±  1544904.394  ops/s
TwoStringConcatenation.testStringUtilsJoin         thrpt   10   17862059.712 ±   951589.994  ops/s
```

# Acknowledgement
* String benchmarks are constructed based on this [repo](https://github.com/derfsubterfuge/string.concat.git).
