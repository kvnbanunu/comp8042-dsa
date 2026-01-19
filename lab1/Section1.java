import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Section1 {
  public static void main(String[] args) {
    MethodTimer timer = new MethodTimer();

    int n = 10;
    // factorial won't work for anything greater due to integer overflow

    Algorithm q1 = new ThreeLoops("q1");
    Algorithm q2 = new Factorial("q2");
    Algorithm q3 = new BinarySearch("q3");

    q1.run(timer, n);
    q2.run(timer, n);
    q3.run(timer, n);
  }
}

abstract class Algorithm {
  public String path;

  public Algorithm(String path) {
    this.path = path + ".csv";
  }

  public void run(MethodTimer timer, int n) {
    for (int i = 1; i <= n; i++) {
      int local = i;
      timer.timeMethod(
          () -> {
            operation(local);
          },
          i);
    }
    try {
      timer.writeResultsToCsv(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void operation(int n) {}
}

// First algorithm for Section 1, I don't know what to call it
class ThreeLoops extends Algorithm {
  public ThreeLoops(String name) {
    super(name);
  }

  @Override
  public void operation(int n) {
    int sum = 0;
    int a = 0;
    int b = 0;
    for (int i = 0; i < n; i++) {
      b += 2;
      for (int j = 0; j < i * i; j++) {
        a++;
        sum += a;
        for (int k = 0; k < j; k++) {
          sum += a + b + 1;
        }
      }
    }

    // Test output
    // System.out.println("q1 n: " + n + " sum: " + sum);
  }
}

class Factorial extends Algorithm {
  public Factorial(String name) {
    super(name);
  }

  @Override
  public void operation(int n) {
    int result = helper(n);

    // Test output
    // System.out.println("q2: n: " + n + " ans: " + result);
  }

  private int helper(int n) {
    if (n == 1) {
      return 1;
    } else {
      return n * helper(n - 1);
    }
  }
}

class BinarySearch extends Algorithm {
  public BinarySearch(String name) {
    super(name);
  }

  @Override
  public void operation(int n) {
    if (n < 0) {
      return;
    }

    int[] arr = new int[n];
    for (int i = 0; i < n; i++) {
      arr[i] = i;
    }

    // gen random int between 0 - n
    int rand = (int) (Math.random() * (n + 1));

    boolean result = helper(arr, rand);

    // Test output
    // System.out.println("q3: n: " + n + " arr: " + arr + " ans: " + result);
  }

  private boolean helper(int[] arr, int item) {
    int len = arr.length;

    if (len == 0) {
      return false;
    }

    int mid = len / 2;

    if (item == arr[mid]) {
      return true;
    }
    if (item > arr[mid]) {
      int[] upper = Arrays.copyOfRange(arr, mid + 1, len);
      return helper(upper, item);
    }
    int[] lower = Arrays.copyOfRange(arr, 0, mid);
    return helper(lower, item);
  }
}

// Changed code to be more reusable so this can be run for all tests
class MethodTimer {
  private final ArrayList<BenchmarkResult> results = new ArrayList<>();

  public void timeMethod(Runnable method, int inputSize) {
    long startTime = System.nanoTime();
    method.run();
    long endTime = System.nanoTime();

    long duration = endTime - startTime;
    results.add(new BenchmarkResult(inputSize, duration));
  }

  public void writeResultsToCsv(String csvFile) throws IOException {
    try (FileWriter writer = new FileWriter(csvFile)) {
      writer.write("InputSize,Duration(ns)\n");
      for (BenchmarkResult result : results) {
        writer.write(result.inputSize + "," + result.duration + "\n");
      }
      resetTimer();
    }
  }

  private void resetTimer() {
    results.clear();
    // I addeed this to ensure the size(mem) actually resets to 0
    // in case not having to resize the arraylist when reusing the timer
    // affects the total runtime
    results.trimToSize();
  }

  private static class BenchmarkResult {
    int inputSize;
    long duration;

    BenchmarkResult(int inputSize, long duration) {
      this.inputSize = inputSize;
      this.duration = duration;
    }
  }
}
