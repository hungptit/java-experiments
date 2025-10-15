package perf.benchmark;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class LoggingPerf {
  private static final Logger logger =
      LoggerFactory.getLogger(LoggingPerf.class);
  private Map<String, String> data;

  @Setup
  public void setupTest() {
    data = new HashMap<>();
    data.put("Foo", "1");
    data.put("Boo", "2");
    data.put("Goo", "3");
    data.put("Hello", "4");
    data.put("Hello world", "5");
    data.put("This is a long string", "6");
  }

  @Benchmark
  public void debugCreateTemporaryObjectUsingPlus(Blackhole bh) {
    logger.debug("This is "
                 + "my "
                 + "test log line: " + data);
  }

  @Benchmark
  public void debugCreateTemporaryObjectUsingFormat(Blackhole bh) {
    logger.debug(
        String.format("%s%s%s%s", "This is ", "my ", "test log line: ", data));
  }

  @Benchmark
  public void debugStandardSyntax(Blackhole bh) {
    logger.debug("{}{}{}{}", "This is ", "my ", "test log line: ", data);
  }

  @Benchmark
  public void debugStandardSyntaxWithIfGuard(Blackhole bh) {
    if (logger.isDebugEnabled()) {
      logger.debug("{}{}{}{}", "This is ", "my ", "test log line: ", data);
    }
  }

  @Benchmark
  public void infoCreateTemporaryObjectUsingPlus(Blackhole bh) {
    logger.info("This is "
                + "my "
                + "test log line: " + data);
  }

  @Benchmark
  public void infoCreateTemporaryObjectUsingFormat(Blackhole bh) {
    logger.info(
        String.format("%s%s%s%s", "This is ", "my ", "test log line: ", data));
  }

  @Benchmark
  public void infoStandardSyntax(Blackhole bh) {
    logger.info("{}{}{}{}", "This is ", "my ", "test log line: ", data);
  }

  @Benchmark
  public void infoStandardSyntaxWithIfGuard(Blackhole bh) {
    if (logger.isInfoEnabled()) {
      logger.info("{}{}{}{}", "This is ", "my ", "test log line: ", data);
    }
  }
}
