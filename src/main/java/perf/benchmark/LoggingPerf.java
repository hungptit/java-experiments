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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class LoggingPerf {
    private static final Logger logger = LoggerFactory.getLogger(LoggingPerf.class);
    Map<String, String> data;
    Level level;
    
    @Setup
    public void setupTest() {
        data = new HashMap<String, String>();
        data.put("Foo", "1");
        data.put("Boo", "2");
        data.put("Goo", "3");
        data.put("Hello", "4");
        data.put("Hello world", "5");
        data.put("This is a long string", "6");

        level = Level.INFO;
        LogManager.getRootLogger().setLevel(level);
    }

    @Benchmark
    public void infoCreateTemporaryObjectUsingPlus(Blackhole bh) {
        logger.info("This is " + "my " + "test log line: " + data.toString());
    }
    
    @Benchmark
    public void infoCreateTemporaryObjectUsingFormat(Blackhole bh) {
        logger.info(String.format("%s%s%s%s", "This is ", "my ", "test log line: ", data.toString()));
    }

    @Benchmark
    public void infoStandardSyntax(Blackhole bh) {
        logger.info("{}{}{}{}", "This is ", "my ", "test log line: ", data);
    }
    
    @Benchmark
    public void warnCreateTemporaryObjectUsingPlus(Blackhole bh) {
        logger.warn("This is " + "my " + "test log line: " + data.toString());
    }
    
    @Benchmark
    public void warnCreateTemporaryObjectUsingFormat(Blackhole bh) {
        logger.warn(String.format("%s%s%s%s", "This is ", "my ", "test log line: ", data.toString()));
    }

    @Benchmark
    public void warnStandardSyntax(Blackhole bh) {
        logger.warn("{}{}{}{}", "This is ", "my ", "test log line: ", data);
    }
    
    @Benchmark
    public void warnStandardSyntaxWithIfGuard(Blackhole bh) {
        if (level.toInt() > Level.INFO.toInt()) {
            logger.warn("{}{}{}{}", "This is ", "my ", "test log line: ", data);
        }
    }
}
