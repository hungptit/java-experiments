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
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class MapDiff {
    private Map<String, String> map1;
    private Map<String, String> map2;

    @Setup
    public void setupTest() {
        map1 = new HashMap<String, String>();
        map2 = new HashMap<String, String>();

        int N1 = 10;
        int N2 = 10;
        int N3 = 100;
        int N4 = 10;

        // Generate items for map1 only
        for (int i = 0; i < N1; ++i) {
            String key = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            System.out.print(key + "==>" + value + "\n");
            map1.put(key, value);
        }

        // Generate items for map2
        for (int i = 0; i < N2; ++i) {
            String key = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            System.out.print(key + "==>" + value + "\n");
            map2.put(key, value);
        }

        // Generate items for both map1 and map2 with the same keys and values.
        for (int i = 0; i < N3; ++i) {
            String key = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            System.out.print(key + "==>" + value + "\n");
            map1.put(key, value);
            map2.put(key, value);
        }

        // Generate items for both map1 and map2 with the same keys.
        for (int i = 0; i < N4; ++i) {
            String key = UUID.randomUUID().toString();
            map1.put(key, UUID.randomUUID().toString());
            map2.put(key, UUID.randomUUID().toString());
        }
    }

    @Benchmark
    public void simpleAlgorithmUsingMapEntry(Blackhole bh) {
        bh.consume(usingMapEntry(map1, map2));
    }

    @Benchmark
    public void simpleAlgorithmUsingKeySet(Blackhole bh) {
        bh.consume(usingKeySet(map1, map2));
    }

    @Benchmark
    public void simpleAlgorithmIterator(Blackhole bh) {
        bh.consume(usingIterator(map1, map2));
    }

    @Benchmark
    public void streamBasedAlgorithm(Blackhole bh) {
        bh.consume(diff2(map1, map2));
    }

    // Simple approach
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

    // Use Sets
    private <K, V> Set<K> diff2(Map<K, V> map1, Map<K, V> map2) {
        Set<K> results = Sets.union(map1.keySet(), map2.keySet())
                             .stream()
                             .filter(key
                                 -> !map1.containsKey(key) || !map2.containsKey(key)
                                     || !map1.get(key).equals(map2.get(key)))
                             .collect(Collectors.toSet());
        return results;
    }
}
