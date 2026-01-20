import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Section3 {

  public static void main(String[] args) {
    int start = 12; // hard set minimum
    int end = 40;
    int len = end - start + 1;
    int[] tests = new int[len];

    for (int i = 0; i < len; i++) {
      Drawer drawer = new Drawer(i + start);
      tests[i] = drawer.drawTillMatch();
      System.out.println("Tries (n=" + (i + start) + "): " + tests[i]);
    }

    double sum = 0;
    int min = end;
    int max = 0;
    for (int t : tests) {
      sum += t;
      min = Math.min(min, t);
      max = Math.max(max, t);
    }

    double avg = sum / (double) len;
    System.out.println("Worst: " + max);
    System.out.println("Best: " + min);
    System.out.println("Average: " + avg);
  }
}

class Drawer {
  List<String> ribbons;

  // n needs to be >= 12 for this exercise to work
  // since we need at least 2 purple socks for a match
  Drawer(int n) {
    String[] arr = new String[n];

    // 1/2 pink
    int mid = n / 2;
    for (int i = 0; i < mid; i++) {
      arr[i] = "pink";
    }

    // 1/3 blue
    int mid2 = n / 3 + mid;
    for (int i = mid; i < mid2; i++) {
      arr[i] = "blue";
    }

    // 1/6 purple (fill the rest)
    for (int i = mid2; i < n; i++) {
      arr[i] = "purple";
    }

    ribbons = new ArrayList<>(Arrays.asList(arr));
    Collections.shuffle(ribbons);
  }

  public int drawTillMatch() {
    int tries = 0;
    HashMap<String, Boolean> seen = new HashMap<>();
    seen.put("pink", false);
    seen.put("blue", false);
    seen.put("purple", false);

    while (true) {
      tries++;
      String ribbon = draw();
      if (seen.get(ribbon)) { // if we already have a matching ribbon
        return tries;
      }
      seen.replace(ribbon, true);
    }
  }

  // draws a ribbon and removes it from the drawer
  private String draw() {
    int len = ribbons.size();
    int rand = (int) (Math.random() * len);

    String res = ribbons.get(rand);
    ribbons.remove(rand);
    return res;
  }

  public void Print() {
    System.out.println(ribbons.toString());
  }
}
