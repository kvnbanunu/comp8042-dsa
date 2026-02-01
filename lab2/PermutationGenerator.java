public class PermutationGenerator {
  private int size;
  private Integer[] inputList;
  private Integer[] interList;

  /*
   * Initialize input and inter list to an array of size n
   * inputList is then filled with values
   * 0..n-1 interList is filled with null values
   */
  public PermutationGenerator(int n) {
    this.size = n;
    this.inputList = new Integer[n];
    this.interList = new Integer[n];

    // fill arr 0..n-1
    for (int i = 0; i < n; i++) {
      this.inputList[i] = i;
    }
  }

  // Sets inputList to the passed in list
  // and interList to an array of the same size with null values.
  public PermutationGenerator(Integer[] inputList) {
    this.inputList = inputList;
    this.interList = new Integer[inputList.length];
    this.size = inputList.length;
  }

  /*
   * returns an array of size n where each index i contains the smallest number
   * that is larger than inputList[i] that is stored in the index past i within
   * the input array, otherwise null.
   * inputList is left unchanged, while interList contains the output and also returned.
   */
  public Integer[] smallestLargerNumbers() {
    int n = this.size;
    Integer[] li = this.inputList;
    Integer[] res = this.interList;
    Integer[] next = new Integer[n];
    int[] inv = new int[n];

    // fill inverse arr [value] = index
    for (int i = 0; i < n; i++) {
      inv[li[i]] = i;
    }

    // init next array (each val replaced with it's increment)
    // last value stays null
    for (int i = 0; i < n - 1; i++) {
      next[i] = i + 1;
    }

    // last one is always null
    res[n - 1] = null;

    // iterate until second last index
    for (int i = 0; i < n - 1; i++) {
      Integer curr = li[i];
      // jump to the next value until it occurs after i or out of bounds
      while (curr != null && inv[curr] <= i) {
        curr = next[curr];
      }

      res[i] = curr;

      // path to next check is compressed
      if (li[i] > 0) { // cannot access ind -1
        next[li[i] - 1] = curr;
      }
    }

    return res;
  }

  /*
   * returns an array of size n where each index i contains the largest number
   * that is smaller than inputList[i] that is stored in the index past i within
   * the input array, otherwise null.
   * inputList is left unchanged, while interList contains the output and also returned.
   */
  public Integer[] largestSmallerNumbers() {
    int n = this.size;
    Integer[] li = this.inputList;
    Integer[] res = this.interList;
    Integer[] next = new Integer[n]; // next smaller value
    int[] inv = new int[n];

    // fill inverse array [value] = index
    for (int i = 0; i < n; i++) {
      inv[li[i]] = i;
    }

    // init next array (each val replaced with its decrement)
    // ind 0 is smallest so stays null
    for (int i = 1; i < n; i++) {
      next[i] = i - 1;
    }

    // last one is always null
    res[n - 1] = null;

    // iterate until second last index
    for (int i = 0; i < n - 1; i++) {
      Integer curr = li[i];
      // jump downward until we find a value occurring after i
      while (curr != null && inv[curr] <= i) {
        curr = next[curr];
      }

      res[i] = curr;

      // path compression for future jumps
      if (li[i] < n - 1) { // cannot access ind n
        next[li[i] + 1] = curr;
      }
    }

    return res;
  }

  /*
   * shuffles the input array by randomly swapping values storing the final
   * result in interList.
   * Both inputList and interList are mutated.
   */
  public void shuffle() {
    this.interList = this.inputList.clone();
    for (int i = 0; i < this.size; i++) {
      // swapping random indexes
      int rand = (int) (Math.random() * this.size);
      int temp = this.interList[i];
      this.interList[i] = this.interList[rand];
      this.interList[rand] = temp;
    }
    this.inputList = this.interList.clone();
  }

  // ---------- The following methods are used for testing

  public void printInput() {
    printList("Input", this.inputList);
  }

  public void printInter() {
    printList("Inter", this.interList);
  }

  static void printList(String title, Integer[] li) {
    String list = title + ": [";

    list += li[0];
    for (int i = 1; i < li.length; i++) {
      list += "," + li[i];
    }
    list += "]";
    P(list);
  }

  static void printOutput(Integer[] li) {
    printList("Output", li);
  }

  static void P(String s) {
    System.out.println(s);
  }

  static void P() {
    System.out.println();
  }

  public static void main(String[] args) {
    P();
    P("Lab example");
    Integer[] labExample = {1, 0, 6, 4, 2, 3, 5}; // lab example
    PermutationGenerator gen = new PermutationGenerator(labExample);
    gen.printInput();
    printOutput(gen.smallestLargerNumbers()); // [2,2,null,5,3,5,null]
    printOutput(gen.largestSmallerNumbers()); // [0,null,5,3,null,null,null]

    P();
    P("Strictly descending array");
    Integer[] strictDesc = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    gen = new PermutationGenerator(strictDesc);
    gen.printInput();
    printOutput(gen.smallestLargerNumbers());
    // [null,null,null,null,null,null,null,null,null,null]
    printOutput(gen.largestSmallerNumbers());
    // [8, 7, 6, 5, 4, 3, 2, 1, 0, null]

    P();
    P("Worst case");
    Integer[] worstCase = {8, 7, 6, 5, 4, 3, 2, 1, 0, 9};
    gen = new PermutationGenerator(worstCase);
    gen.printInput();
    printOutput(gen.smallestLargerNumbers());
    // [9,9,9,9,9,9,9,9,9,null]
    printOutput(gen.largestSmallerNumbers());
    // [7,6,5,4,3,2,1,0,null,null]

    P();

    // gen = new PermutationGenerator(10);
    // gen.printInput();
    // gen.printInter();
    // gen.shuffle();
    // gen.printInter();
  }
}
