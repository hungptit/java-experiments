class: center, middle
# Java optimization tips (Part I)

<!-- How many of you are writing code in Java daily? --> 

<!-- You might know some performance tips that I am going to show,
however, I hope you can find something new today -->

<!-- Feel free to raise your hand if you have any
question/suggestion. If I cannot answer your question directly then I
will try to resolve it offline -->

<!-- Why there are two parts -->

<!-- First part: Practical tips that can be applied directly to your
code base. Share the lessons that we have learned after speeding up
our Java backend service for more than 10x -->

<!-- Second part: How can we improve the performance of our backend
services by changing our design and technologies -->

---
background-image: url(https://www.allearsenglish.com/wp-content/uploads/2014/02/why-you-cant-speak-English.jpg)

<!-- Scale vertically i.e better throughput and lower latency -->
<!-- And from the business point of view: We spend less and gain more -->

---

background-image: url(https://www.decentjobsforyouth.org/images/goal/Goals.jpg)

<!-- Show tips that can be used to improve the performance of any Java
code base. Some tips can be applied to any language.  -->

<!-- Each tip will come with a story and practical benchmarks -->

---
class: center, middle
# #1 - [Everything Should Be Made as Simple as Possible, But Not Simpler](https://en.wikiquote.org/wiki/Albert_Einstein)

<!-- Performance tuning is a refactoring process which aims to improve
the performance of our codebase or services -->

<!-- 1. We can only tune our code if we understand it so code must be
as simple as possible -->

<!-- 2. This is a 1-1 transformation i.e no new feature is added so we
do need a good set of unit/integ tests to be able to proceed.  -->

---
class: center, middle
# #2 - Measure, Measure, and Measure

<!-- Forget about all of your intuitions. The modern computer
architecture is complex and the only intuition is to measure it. -->

---
# #3 - Use a profiler to find the real bottleneck

--

- [VisualVM](https://visualvm.github.io/)

<!-- This is a decent profiler for Java and it is free. I know that we
do have commercial license for some Java profiler internally, however,
from what I have known features that these profilers can offer is not
much different from VisualVM. So try VisualVM first to see if it fit
for your need and I bet it will. -->

--

- [perf](https://perf.wiki.kernel.org/index.php/Main_Page)

<!-- Allow use to trace the performance of our services at the kernel
level. -->

--

- [strace](https://linux.die.net/man/1/strace)

<!-- Very useful tool for tracing IO activities. I have used strace to
reverse engineer the open source command i.e ripgrep -->

--

- Use micro-benchmark framework such as [jmh](https://openjdk.java.net/projects/code-tools/jmh/)

<!-- This is a must have tool which allows you to measure and to track
the performance of your critical/hot functions or methods. -->

---
# #4 - Build performance test suite for your services/apps

--

- Use automated performance tests to detect performance regression.

<!-- Tip: I have been using Jenkins to spot the potential performance
issues i.e see the unit test time increase significantly in a
branch. -->

--

- Run your performance test regularly to detect performance issue as early as you can.

---
# #5 - Troubleshooting

--

- Tackle the biggest bottleneck first.

--

- Do not make any assumption. **You have to measure it.**

---
# #6 - Asynchronous vs Synchronous services

--
- Consider asynchronous programming for your backend applications/services. Check out [vertx](https://vertx.io/) or [netty](https://netty.io/) for more information.

<!-- Each approach has it own usecases, however, consider the
asynchronous programming if your execution tasks can be executed
asynchronously. -->

---
# #7 - Caching

--

- Cache expensive resources such as database connections.

--

- Cache the precomputed results if necessary for example hash codes of your **heavy** objects.

<!-- Or we have used a multimap to speed up the look up performance
for items in a list (note that elements of this list are not modified
frequently). -->

---
# #8 - Logging

--
- Set the level of your log item **properly**.

<!-- This is language independent. We did remove unneccesary log items
in our Perl codebase to make sure we did not stress out scribe
logger. -->

--

- Do not use String.format to create the log messages
``` java
logger.info(String.format("%s %s %s", "foo", "boo", myObj.toString()));
```

--

- Do not need to check the logging level before issueing any logging call.
``` java
if (level.isDebugEnabled()) {
    logger.debug("{}{}{}{}", "This is ", "my ", "test log line: ", data);
}
```

<!-- We can find this suggestion somewhere in the web, however, it is
not true if you use the logging methods properly i.e do not construct
the temporary objects in your logging calls. -->

<!-- How do we know? -->

<!-- Practical examples + benchmark results -->

---
# #8 - Logging - Benchmark results

--

- Executed command
``` java
java -jar target/experiments-0.0.1-SNAPSHOT.jar Logging > /dev/shm/hdang/output.log
```

--

- Benchmark results
``` shell
Benchmark                                           Mode  Cnt          Score        Error  Units
LoggingPerf.debugCreateTemporaryObjectUsingFormat  thrpt   10     492337.690 ±   9998.527  ops/s
LoggingPerf.debugCreateTemporaryObjectUsingPlus    thrpt   10    2586671.576 ±  16277.296  ops/s
LoggingPerf.debugStandardSyntax                    thrpt   10  220863948.357 ± 376326.966  ops/s
LoggingPerf.debugStandardSyntaxWithIfGuard         thrpt   10  218783853.648 ± 259205.900  ops/s
LoggingPerf.infoCreateTemporaryObjectUsingFormat   thrpt   10     201335.590 ±   5143.631  ops/s
LoggingPerf.infoCreateTemporaryObjectUsingPlus     thrpt   10     333285.530 ±   7465.220  ops/s
LoggingPerf.infoStandardSyntax                     thrpt   10     312504.657 ±   7984.738  ops/s
LoggingPerf.infoStandardSyntaxWithIfGuard          thrpt   10     301083.544 ±  10608.708  ops/s
```

---
# #9 - Storing and exchanging data

--

- Use binary format to store/exchange your data if you can. Take a look at matured data format such as [protobuf](https://developers.google.com/protocol-buffers/), [Thrift](https://github.com/apache/thrift), or [message-pack](https://msgpack.org/) for detail information.

<!-- There are many reasons for using binary format such as compact size and better performance. -->

--

- If you have to use JSON then use optimized Java JSON libraries if you can. According to [this article](https://dzone.com/articles/is-protobuf-5x-faster-than-json-part-ii) both [jsoniter](https://jsoniter.com/) or [Jackson](https://github.com/FasterXML/jackson)'s performance is comparable to that of [Thrift](https://thrift.apache.org/).

<!-- We have sped up our code about 2x or more by replacing Gson with Jsoniter. -->

---
# #9 - Primitive data benchmark results

--

- int
``` shell
Benchmark            Mode   Cnt Score        Error      Units
DeserThrift.deser    avgt    5  34550.722  ± 9362.997   ns/op
DeserJackson.deser   avgt    5  253647.104 ± 36337.893  ns/op
DeserJsoniter.deser  avgt    5  48213.617  ± 14037.734  ns/op
```

--

- double
``` shell
Benchmark            Mode   Cnt Score        Error       Units
DeserThrift.deser    avgt    5  33595.775  ± 14478.284   ns/op
DeserJackson.deser   avgt    5  693700.291 ± 45516.568   ns/op
DeserJsoniter.deser  avgt    5  50072.716  ± 12903.108   ns/op
```

--

- One string
``` shell
Benchmark            Mode  Cnt      Score      Error     Units
DeserThrift.deser    avgt    5  76742.102   ± 3751.229     ns/op
DeserJackson.deser   avgt    5  266844.618  ± 141765.040  ns/op
DeserJsoniter.deser  avgt    5  45290.497   ± 3282.505     ns/op
```

---
# #9 - Five string benchmarks

--

- Deserialization
``` Shell
Benchmark            Mode   Cnt Score        Error      Units
DeserThrift.deser    avgt    5  324088.201 ± 19773.342  ns/op
DeserJackson.deser   avgt    5  458724.474 ± 27443.272  ns/op
DeserJsoniter.deser  avgt    5  239931.887 ± 82102.165  ns/op
```

--

- Serialization
``` shell
Benchmark        Mode   Cnt Score        Error       Units
SerThrift.ser    avgt    5  362863.474 ± 195440.889  ns/op
SerJackson.ser   avgt    5  328474.007 ± 68095.593   ns/op
SerJsoniter.ser  avgt    5  111447.889 ± 11684.470   ns/op
```

---
# #10 - String concatenation

--

- Do not use **String.format** for string concatenation.

--

- Use StringBuilder for string concatenation.
``` java
String buffer = (new StringBuilder("foo"))
		.append("+")
		.append("boo")
		.toString();
```

--

- Consider "+" for one-line string concatenation.
``` java
String buf = "foo" + "boo";
```

---
# #10 - Two string concatenation benchmark results

```shell
Benchmark                                      Mode  Cnt         Score         Error  Units
TwoStringConcatenation.testGuavaJoiner        thrpt   10   6419391.451 ±  190766.677  ops/s
TwoStringConcatenation.testPlus               thrpt   10  26591547.259 ± 1012492.069  ops/s
TwoStringConcatenation.testStreamJoining      thrpt   10   6477426.394 ±  339046.612  ops/s
TwoStringConcatenation.testStringBuffer       thrpt   10  29862827.101 ±  688383.774  ops/s
TwoStringConcatenation.testStringBuilder      thrpt   10  28419281.812 ± 6316187.196  ops/s
TwoStringConcatenation.testStringConcat       thrpt   10  26838996.508 ±  450626.269  ops/s
TwoStringConcatenation.testStringFormat       thrpt   10   1127386.968 ±   28269.536  ops/s
TwoStringConcatenation.testStringJoin         thrpt   10  10501709.950 ±  188915.321  ops/s
TwoStringConcatenation.testStringUtilsJoin    thrpt   10   8544432.132 ±  150001.112  ops/s
```

---
# #10 - Three string concatenation benchmark results

```shell
Benchmark                                      Mode  Cnt         Score         Error  Units
ThreeStringConcatenation.testGuavaJoiner      thrpt   10   4449592.278 ±   15755.036  ops/s
ThreeStringConcatenation.testPlus             thrpt   10  20192579.476 ±  343545.435  ops/s
ThreeStringConcatenation.testStreamJoining    thrpt   10   4842927.872 ±  101167.982  ops/s
ThreeStringConcatenation.testStringBuffer     thrpt   10  24259235.032 ±  915967.356  ops/s
ThreeStringConcatenation.testStringBuilder    thrpt   10  20495648.692 ± 3354368.257  ops/s
ThreeStringConcatenation.testStringConcat     thrpt   10  14680161.500 ±  471591.794  ops/s
ThreeStringConcatenation.testStringFormat     thrpt   10    728541.505 ±  108856.425  ops/s
ThreeStringConcatenation.testStringJoin       thrpt   10   7113841.104 ±  282227.191  ops/s
ThreeStringConcatenation.testStringUtilsJoin  thrpt   10   6708409.144 ±  958845.486  ops/s
```

---
# #10 - Five string concatenation benchmark results

```shell
Benchmark                                      Mode  Cnt         Score         Error  Units
FiveStringConcatenation.testGuavaJoiner       thrpt   10   2911366.372 ±   13875.703  ops/s
FiveStringConcatenation.testPlus              thrpt   10  13832024.846 ±  228485.847  ops/s
FiveStringConcatenation.testStreamJoining     thrpt   10   3391921.391 ±   50233.733  ops/s
FiveStringConcatenation.testStringBuffer      thrpt   10  15299557.535 ±   51062.787  ops/s
FiveStringConcatenation.testStringBuilder     thrpt   10  15290783.678 ±  402331.431  ops/s
FiveStringConcatenation.testStringConcat      thrpt   10   6483536.547 ±  283871.792  ops/s
FiveStringConcatenation.testStringFormat      thrpt   10    487832.645 ±   14242.885  ops/s
FiveStringConcatenation.testStringJoin        thrpt   10   4734382.678 ±  714679.091  ops/s
FiveStringConcatenation.testStringUtilsJoin   thrpt   10   4287492.852 ±  393455.865  ops/s
```

---
# #10 - Ten string concatenation benchmark results

``` shell
Benchmark                                      Mode  Cnt         Score         Error  Units
TenStringConcatenation.testGuavaJoiner        thrpt   10   1036585.464 ±  104158.035  ops/s
TenStringConcatenation.testPlus               thrpt   10   4173151.866 ±  480400.987  ops/s
TenStringConcatenation.testStreamJoining      thrpt   10   1140375.972 ±   83964.244  ops/s
TenStringConcatenation.testStringBuffer       thrpt   10   6022510.891 ± 1587127.273  ops/s
TenStringConcatenation.testStringBuilder      thrpt   10   6833821.783 ± 1470062.295  ops/s
TenStringConcatenation.testStringConcat       thrpt   10   2233894.152 ±  422994.505  ops/s
TenStringConcatenation.testStringFormat       thrpt   10    267635.787 ±    5517.750  ops/s
TenStringConcatenation.testStringJoin         thrpt   10   2596763.302 ±   44601.412  ops/s
TenStringConcatenation.testStringUtilsJoin    thrpt   10   2288520.438 ±   67909.212  ops/s
```

---
# #11 - Use primitive and stack

--
``` java
// Goes to the heap
Integer i = 817598;

// Stays on the stack
int i = 817598;

// Three heap objects!
Integer[] i = { 1337, 424242 };

// One heap object.
int[] i = { 1337, 424242 };
```

---
# #12 - Use entrySet

--

- Don't do this
``` java
for (K key : map.keySet()) {
    V value = map.get(key);
}
```

--

- Do this
``` java
for (Entry<K, V> entry : map.entrySet()) {
    K key = entry.getKey();
    V value = entry.getValue();
}
```

---
# #12 - Use keySet (cont)

--
``` java
    private <K, V> Set<K> usingKeySet(Map<K, V> map1, Map<K, V> map2) {
        Set<K> results = new HashSet<K>();

        // Find all keys in map1 which either are not present in map2
        // or have different values.
        for (var key : map1.keySet()) {
            if (map2.containsKey(key)) {
                if (!map1.get(key).equals(map2.get(key))) {
                    results.add(key);
                }
            } else {
                results.add(key);
            }
        }

        // Find all keys in map2 that are not in map1.
        for (var key : map2.keySet()) {
            if (!map1.containsKey(key)) {
                results.add(key);
            }
        }
        return results;
    }
```

---
# #12 - Use entrySet (cont)

--
``` java
    private <K, V> Set<K> usingMapEntry(Map<K, V> map1, Map<K, V> map2) {
        Set<K> results = new HashSet<K>();

        // Find all keys in map1 which either are not present in map2
        // or have different values.
        for (Map.Entry<K, V> entry : map1.entrySet()) {
            K key = entry.getKey();
            if (map2.containsKey(key)) {
                if (!entry.getValue().equals(map2.get(key))) {
                    results.add(key);
                }
            } else {
                results.add(key);
            }
        }

        // Find all keys in map2 that are not in map1.
        for (Map.Entry<K, V> entry : map2.entrySet()) {
            K key = entry.getKey();
            if (!map1.containsKey(key)) {
                results.add(key);
            }
        }
        return results;
    }
```

---
# #12 - Benchmark results

--

``` java
Benchmark                              Mode  Cnt       Score      Error  Units
MapDiff.simpleAlgorithmUsingKeySet    thrpt   10  187122.223 ± 8303.220  ops/s
MapDiff.simpleAlgorithmUsingMapEntry  thrpt   10  207194.485 ± 4506.768  ops/s
```

---
# #13 - [Iterator](https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html) is not slow

--
- [entrySet is implemeted using iterator](http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/util/AbstractMap.java#l327)

---
# #13 - [Iterator](https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html) is not slow (cont)

--
``` java
    private <K, V> Set<K> usingIterator(Map<K, V> map1, Map<K, V> map2) {
        Set<K> results = new HashSet<K>();

        // Find all keys in map1 which either are not present in map2
        // or have different values.
        Iterator<Map.Entry<K, V>> entries = map1.entrySet().iterator();
        while (entries.hasNext()) {
            var entry = (Map.Entry<K, V>) entries.next();
            K key = entry.getKey();
            if (map2.containsKey(key)) {
                if (!entry.getValue().equals(map2.get(key))) {
                    results.add(key);
                }
            } else {
                results.add(key);
            }
        }

        // Find all keys in map2 that are not in map1.
        entries = map2.entrySet().iterator();
        while (entries.hasNext()) {
            var entry = (Map.Entry<K, V>) entries.next();
            K key = entry.getKey();
            if (!map1.containsKey(key)) {
                results.add(key);
            }
        }

        return results;
    }
```

---
# #13 - Benchmark results

--
``` java
Benchmark                              Mode  Cnt       Score       Error  Units
MapDiff.simpleAlgorithmIterator       thrpt   10  207181.447 ±  4438.118  ops/s
MapDiff.simpleAlgorithmUsingKeySet    thrpt   10  188632.820 ±  2010.983  ops/s
MapDiff.simpleAlgorithmUsingMapEntry  thrpt   10  197961.092 ± 13139.091  ops/s
```

---
# #14 - Use [EnumSet](https://docs.oracle.com/javase/7/docs/api/java/util/EnumSet.html) or [EnumMap](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html)

--

- Use EnumSet and EnumMap if the number of possible keys is known in advance.

---
# #14 - Use [EnumSet](https://docs.oracle.com/javase/7/docs/api/java/util/EnumSet.html) or [EnumMap](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html)

--

``` java
    private enum Constants {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE;
    }
    private enum Strings {
        ONE("One"),
        TWO("Two"),
        THREE("Three"),
        FOUR("Four"),
        FIVE("Five");
        private final String value;
        Strings(final String s) {
            value = s;
        }
    }
```

---
# #14 - Use [EnumSet](https://docs.oracle.com/javase/7/docs/api/java/util/EnumSet.html) or [EnumMap](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html) (cont)

``` java
    @Setup
    public void setupTest() {
        set1 = EnumSet.of(Constants.ONE, Constants.TWO, Constants.THREE, Constants.FIVE);
        set2 = new HashSet<Constants>();
        set2.add(Constants.ONE);
        set2.add(Constants.TWO);
        set2.add(Constants.THREE);
        set2.add(Constants.FOUR);

        sset1 = EnumSet.of(Strings.ONE, Strings.TWO, Strings.THREE, Strings.FIVE);
        sset2 = new HashSet<Strings>();
        sset2.add(Strings.ONE);
        sset2.add(Strings.TWO);
        sset2.add(Strings.THREE);
        sset2.add(Strings.FIVE);
    }
```

---
# #14 - Use [EnumSet](https://docs.oracle.com/javase/7/docs/api/java/util/EnumSet.html) or [EnumMap](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html) (cont)

``` java
    private boolean test(Set<Constants> data) {
        return data.contains(Constants.ONE) && data.contains(Constants.FOUR)
            && data.contains(Constants.FIVE);
    }

    private boolean test_string(Set<Strings> data) {
        return data.contains(Strings.ONE) && data.contains(Strings.FOUR)
            && data.contains(Strings.FIVE);
    }
```

---
# #14 - Use [EnumSet](https://docs.oracle.com/javase/7/docs/api/java/util/EnumSet.html) or [EnumMap](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html) (cont)

--

- Benchmark results collected using my MacBook pro
``` java
Benchmark                     Mode  Cnt          Score          Error  Units
EnumSetAndMap.enumSet        thrpt   10  173328999.224 ± 10238653.535  ops/s
EnumSetAndMap.enumSetString  thrpt   10  187219253.839 ± 11308771.022  ops/s
EnumSetAndMap.hashSet        thrpt   10   91525162.604 ±  6986844.377  ops/s
EnumSetAndMap.hashSetString  thrpt   10  115304652.692 ±  4770025.013  ops/s
```

--

- Benchmark results collected using our development servers
``` java
Benchmark                     Mode  Cnt          Score         Error  Units
EnumSetAndMap.enumSet        thrpt   10  135931456.609 ± 2242331.602  ops/s
EnumSetAndMap.enumSetString  thrpt   10  136029515.802 ± 2724186.710  ops/s
EnumSetAndMap.hashSet        thrpt   10   63530193.770 ±   22918.263  ops/s
EnumSetAndMap.hashSetString  thrpt   10   84277666.685 ±   13520.770  ops/s
```

---
# #15 - Functional programming is not a silver bullet

``` java
    private <K, V> Set<K> diff2(Map<K, V> map1, Map<K, V> map2) {
        Set<K> results = Sets.union(map1.keySet(), map2.keySet())
                             .stream()
                             .filter(key
                                     -> !map1.containsKey(key) || !map2.containsKey(key) ||
                                            !map1.get(key).equals(map2.get(key)))
                             .collect(Collectors.toSet());
        return results;
    }
```

---
# #15 - Functional programming is not a silver bullet (cont)

``` java
    private <K, V> Set<K> diff1(Map<K, V> map1, Map<K, V> map2) {
        Set<K> results = new HashSet<K>();

        // Find all keys in map1 which either are not present in map2
        // or have different values.
        for (Map.Entry<K, V> entry : map1.entrySet()) {
            K key = entry.getKey();
            if (map2.containsKey(key)) {
                if (!entry.getValue().equals(map2.get(key))) {
                    results.add(key);
                }
            } else {
                results.add(key);
            }
        }

        // Find all keys in map2 that are not in map1.
        for (Map.Entry<K, V> entry : map2.entrySet()) {
            K key = entry.getKey();
            if (!map1.containsKey(key)) {
                results.add(key);
            }
        }
        return results;
    }
```

---
# #15 - Functional programming is not a silver bullet (cont)

--

- Benchmark results collected in our development servers

``` java
MapDiff.simpleAlgorithm                       thrpt   10    800325.609 ±  242560.538  ops/s
MapDiff.streamBasedAlgorithm                  thrpt   10    415182.813 ±   30691.713  ops/s
```

--

- Benchmark results collected using my MacBook pro
``` java
MapDiff.simpleAlgorithm                       thrpt   10   1364742.826 ±  115566.319  ops/s
MapDiff.streamBasedAlgorithm                  thrpt   10    539597.117 ±   28011.441  ops/s
```

---
# #16 - Optimize your hashCode() and equals() methods

``` java
    @Override
    public int hashCode() {
        return sessionIdHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        var other = (Host) obj;
        return sessionId.equals(other.sessionId);
    }
```

---
# #17 - TODO: Use final keyword


---
# #17 - Common performance tips

--

- Use the right data structure for your need.

--

- Date transport and manipulation is not cheep.
  
--

- Regular expression are useful, but they come at a price.

    - Consider to cache the regular expression pattern to improve the peformance.

---
class: center, middle
# Live demo using production Java repositories

---
class: center, middle
# Q/A

---
# Acknowledgment

--

- Mia Morretti, Zac Bentley (@zbentley), and Anh Pham for valuable suggestions.

---
# References
* Effective Java 3rd, Joshua Bloch.
* Java Performance: The Definitive Guide: Getting the Most Out of Your Code 1st, Scott Oaks.
* [VisualVM](https://visualvm.github.io/)
* [perf](https://perf.wiki.kernel.org/index.php/Main_Page)
* [strace](http://man7.org/linux/man-pages/man1/strace.1.html)
* [Java String Concatenation: Which Way Is Best?](https://redfin.engineering/java-string-concatenation-which-way-is-best-8f590a7d22a8)
* [Java performance optimization tips](https://raygun.com/blog/java-performance-optimization-tips/)
* [Top 10 easy performance optimisations in Java.](https://blog.jooq.org/2015/02/05/top-10-easy-performance-optimisations-in-java/)
* [Java performance tunning](https://stackify.com/java-performance-tuning/)
* [entrySet](https://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-java-map)
* [Simplicity](https://landing.google.com/sre/sre-book/chapters/simplicity/)
* [10 subtle best practices when coding java.](https://blog.jooq.org/2013/08/20/10-subtle-best-practices-when-coding-java/)
