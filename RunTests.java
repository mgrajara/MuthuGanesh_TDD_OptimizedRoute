package Airport.Baggage.PathFinding;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTests {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(OptimizedRouteTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
  }
}
