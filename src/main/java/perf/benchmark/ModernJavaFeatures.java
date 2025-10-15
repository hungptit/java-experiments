package perf.benchmark;

import java.util.List;
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

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class ModernJavaFeatures {
  // Modern Java Record
  public record Person(String name, int age, String email) {
    public boolean isAdult() { return age >= 18; }
  }

  // Modern enum with enhanced features
  public enum Status {
    PENDING("Pending", 1),
    PROCESSING("Processing", 2),
    COMPLETED("Completed", 3),
    FAILED("Failed", 4);

    private final String description;
    private final int priority;

    Status(String description, int priority) {
      this.description = description;
      this.priority = priority;
    }

    public String getDescription() { return description; }
    public int getPriority() { return priority; }
  }

  private List<Person> people;
  private Map<String, Status> statusMap;
  private String jsonTemplate;

  @Setup
  public void setupTest() {
    people = List.of(new Person("Alice", 25, "alice@example.com"),
                     new Person("Bob", 17, "bob@example.com"),
                     new Person("Charlie", 30, "charlie@example.com"),
                     new Person("Diana", 22, "diana@example.com"),
                     new Person("Eve", 16, "eve@example.com"));

    statusMap = Map.of("task1", Status.PENDING, "task2", Status.PROCESSING,
                       "task3", Status.COMPLETED, "task4", Status.FAILED);

    // Modern text block
    jsonTemplate = """
            {
                "name": "%s",
                "age": %d,
                "email": "%s",
                "status": "%s"
            }
            """;
  }

  @Benchmark
  public void testRecordAccess(Blackhole bh) {
    for (var person : people) {
      bh.consume(person.name());
      bh.consume(person.age());
      bh.consume(person.email());
      bh.consume(person.isAdult());
    }
  }

  @Benchmark
  public void testRecordToString(Blackhole bh) {
    for (var person : people) {
      bh.consume(person.toString());
    }
  }

  @Benchmark
  public void testSwitchExpression(Blackhole bh) {
    for (var status : statusMap.values()) {
      var result = switch (status) {
        case PENDING -> "Waiting to start";
        case PROCESSING -> "Currently running";
        case COMPLETED -> "Successfully finished";
        case FAILED -> "Encountered an error";
      };
      bh.consume(result);
    }
  }

  @Benchmark
  public void testSwitchExpressionWithYield(Blackhole bh) {
    for (var status : statusMap.values()) {
      var result = switch (status) {
        case PENDING -> {
          yield "Task is waiting: " + status.getDescription();
        }
        case PROCESSING -> {
          yield "Task is running with priority: " + status.getPriority();
        }
        case COMPLETED -> {
          yield "Task completed successfully";
        }
        case FAILED -> {
          yield "Task failed and needs attention";
        }
      };
      bh.consume(result);
    }
  }

  @Benchmark
  public void testTextBlockFormatting(Blackhole bh) {
    for (var person : people) {
      var json =
          jsonTemplate.formatted(person.name(), person.age(), person.email(),
                                 person.isAdult() ? "adult" : "minor");
      bh.consume(json);
    }
  }

  @Benchmark
  public void testStreamWithRecords(Blackhole bh) {
    var adults =
        people.stream().filter(Person::isAdult).map(Person::name).toList();
    bh.consume(adults);
  }

  @Benchmark
  public void testPatternMatching(Blackhole bh) {
    for (var person : people) {
      var message = switch (person.age()) {
        case 16, 17 -> "Teenager: " + person.name();
        case 18, 19, 20 -> "Young adult: " + person.name();
        // case int age when age >= 21 && age < 30 -> "Adult: " + person.name();
        // case int age when age >= 30 -> "Mature: " + person.name();
        default -> "Child: " + person.name();
      };
      bh.consume(message);
    }
  }

  @Benchmark
  public void testSealedClassBehavior(Blackhole bh) {
    // Simulating sealed class behavior with records
    var shapes = List.of(new Circle(5.0), new Rectangle(3.0, 4.0),
                         new Triangle(3.0, 4.0, 5.0));

    for (var shape : shapes) {
      var area = switch (shape) {
        case Circle c -> Math.PI* c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> {
          var s = (t.a() + t.b() + t.c()) / 2;
          yield Math.sqrt(s * (s - t.a()) * (s - t.b()) * (s - t.c()));
        }
        default -> 0.0;  // This should never be reached due to sealed interface
      };
      bh.consume(area);
    }
  }

  // Sealed interface simulation with records
  public sealed interface Shape permits Circle, Rectangle, Triangle {}
  public record Circle(double radius) implements Shape {}
  public record Rectangle(double width, double height) implements Shape {}
  public record Triangle(double a, double b, double c) implements Shape {}
}
