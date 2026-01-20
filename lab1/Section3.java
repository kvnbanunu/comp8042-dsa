import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Section3 {

  public static void main(String[] args) {
    Drawer drawer = new Drawer(12);
    drawer.Print();
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

    ribbons = Arrays.asList(arr);
    Collections.shuffle(ribbons);
  }

  public void drawTillMatch() {}

  public void Print() {
    System.out.println(ribbons.toString());
  }
}
