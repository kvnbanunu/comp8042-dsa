import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Section2 {
  public static void main(String[] args) {
    int n = 40;
    MethodTimer timer = new MethodTimer(n);

    int[] arr = new int[n];
    for (int i = 0; i < n; i++) {
      arr[i] = i;
    }

    Algorithm[] algos = new Algorithm[4];

    algos[0] = new ThreeLoops("q1");
    algos[1] = new Factorial("q2");
    algos[2] = new BinarySearch("q3", arr);
    algos[3] = new QuinternarySearch("q4", arr);

    for (int i = 0; i < 4; i++) {
      algos[i].run(timer, n);
    }
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
    if (n == 0) {
      return;
    }
    BigInteger result = helper(BigInteger.valueOf(n));

    // Test output
    // System.out.println("q2: n: " + n + " ans: " + result);
  }

  private BigInteger helper(BigInteger n) {
    if (n.equals(BigInteger.ONE)) {
      return BigInteger.ONE;
    } else {
      return helper(n.subtract(BigInteger.ONE)).multiply(n);
    }
  }
}

class BinarySearch extends Algorithm {
  int[] arr;

  public BinarySearch(String name, int[] arr) {
    super(name);
    this.arr = arr;
  }

  @Override
  public void operation(int n) {
    if (n < 0) {
      return;
    }

    // gen random int between 0 - 2n (to produce some fails)
    int target = (int) (Math.random() * (n * 2));

    // cut down the array to match n length
    int[] local = Arrays.copyOfRange(arr, 0, n);

    boolean result = helper(local, target);

    // Test output
    // System.out.println("q3: n: " + n + " arr: " + Arrays.toString(local) + " ans: " + result);
  }

  private boolean helper(int[] arr, int target) {
    int len = arr.length;

    if (len == 0) {
      return false;
    }

    int mid = len / 2;

    if (target == arr[mid]) {
      return true;
    }
    if (target > arr[mid]) {
      int[] upper = Arrays.copyOfRange(arr, mid + 1, len);
      return helper(upper, target);
    }
    int[] lower = Arrays.copyOfRange(arr, 0, mid);
    return helper(lower, target);
  }
}

class QuinternarySearch extends Algorithm {
  int[] arr;

  QuinternarySearch(String name, int[] arr) {
    super(name);
    this.arr = arr;
  }

  @Override
  public void operation(int n) {
    // Same as binary search
    int[] local = Arrays.copyOfRange(arr, 0, n);
    int target = (int) (Math.random() * (n * 2));
    boolean result = helper(local, target);

    // Test output
    // System.out.println("q4: n: " + n + " target: " + target + " ans: " + result);
  }

  private boolean helper(int[] arr, int target) {
    int len = arr.length;

    if (len == 0) {
      return false;
    }

    // base case will be if len is < 5
    if (len < 5) {
      for (int i = 0; i < len; i++) {
        if (arr[i] == target) {
          return true;
        }
      }
      return false;
    }

    // how big each section should be
    int step = (int) (len / 5);

    for (int i = 0; i < 5; i++) {
      int start = i * step;
      int end;

      if (i == 4) { // last split
        end = len;
      } else {
        end = (i + 1) * step;
      }

      if (target == arr[start]) {
        return true;
      }

      if (target > arr[end - 1]) {
        continue;
      }

      int[] local = Arrays.copyOfRange(arr, start, end);
      if (helper(local, target)) {
        return true;
      }
    }

    return false;
  }
}

// Changed code to be more reusable so this can be run for all tests
class MethodTimer {
  private ArrayList<BenchmarkResult> results;

  MethodTimer(int size) {
    results = new ArrayList<>(size);
  }

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
