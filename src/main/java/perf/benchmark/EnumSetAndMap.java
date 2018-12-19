package perf.benchmark;

// For jhm
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

// Map and Set
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class EnumSetAndMap {
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

    private Set<Constants> set1;
    private Set<Constants> set2;
    private Set<Strings> sset1;
    private Set<Strings> sset2;

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

    @Benchmark
    public void enumSet(Blackhole bh) {
        bh.consume(test(set1));
    }

    @Benchmark
    public void hashSet(Blackhole bh) {
        bh.consume(test(set2));
    }

    @Benchmark
    public void enumSetString(Blackhole bh) {
        bh.consume(test_string(sset1));
    }

    @Benchmark
    public void hashSetString(Blackhole bh) {
        bh.consume(test_string(sset2));
    }

    private boolean test(Set<Constants> data) {
        return data.contains(Constants.ONE) && data.contains(Constants.FOUR)
            && data.contains(Constants.FIVE);
    }
    
    private boolean test_string(Set<Strings> data) {
        return data.contains(Strings.ONE) && data.contains(Strings.FOUR)
            && data.contains(Strings.FIVE);
    }
}
