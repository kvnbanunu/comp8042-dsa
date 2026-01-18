import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Section1 {
  public static void main(String[] args) {
    // MethodTimer timer = new MethodTimer();
  }
}

// Changed code to be more reusable so this can be run for all tests
class MethodTimer {
  private final ArrayList<BenchmarkResult> results = new ArrayList<>();

  public void run(Runnable method, int n, String csvFile) {
    for (int i = 1; i <= n; i++) {
      timeMethod(method, i);
    }
    try {
      writeResultsToCsv(csvFile);
      resetTimer();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    }
  }

  public void resetTimer() {
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
