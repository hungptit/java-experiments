package perf.benchmark;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class ThreeStringConcatenation {
    private String s0;
    private String s1;
    private String s2;

    @Setup
    public void setupTest() {
        s0 = UUID.randomUUID().toString();
        s1 = UUID.randomUUID().toString();
        s2 = UUID.randomUUID().toString();
    }

    @Benchmark
    public void testStringFormat(Blackhole bh) {
        String combined =
            String.format("%s%s%s", s0, s1, s2);
        bh.consume(combined);
    }

    @Benchmark
    public void testPlus(Blackhole bh) {
        String combined = s0 + s1 + s2;
        bh.consume(combined);
    }

    @Benchmark
    public void testStringBuilder(Blackhole bh) {
        StringBuilder sb = new StringBuilder()
                               .append(s0)
                               .append(s1)
                               .append(s2);
        bh.consume(sb.toString());
    }

    @Benchmark
    public void testStringBuffer(Blackhole bh) {
        StringBuffer sb = new StringBuffer(s0)
                              .append(s0)
                              .append(s1);
        bh.consume(sb.toString());
    }

    @Benchmark
    public void testStringJoin(Blackhole bh) {
        String combined = String.join("", s0, s1, s2);
        bh.consume(combined);
    }

    @Benchmark
    public void testStringConcat(Blackhole bh) {
        String combined =
            s0.concat(s1).concat(s2);
        bh.consume(combined);
    }

    @Benchmark
    public void testStringUtilsJoin(Blackhole bh) {
        String combined = StringUtils.join(s0, s1, s2);
        bh.consume(combined);
    }

    @Benchmark
    public void testGuavaJoiner(Blackhole bh) {
        String combined = Joiner.on("").join(s0, s1, s2);
        bh.consume(combined);
    }

    @Benchmark
    public void testStreamJoining(Blackhole bh) {
        String combined = Arrays.asList(s0, s1, s2).stream().collect(Collectors.joining());
        bh.consume(combined);
    }
}
