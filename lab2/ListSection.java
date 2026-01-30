class PermutationGenerator {
  private int size;
  private Integer[] inputList;
  private Integer[] interList;

  public PermutationGenerator(int n) {
    this.size = n;
    this.inputList = new Integer[n];
    this.interList = new Integer[n];

    for (int i = 0; i < n; i++) {
      this.inputList[i] = i;
    }
  }

  public PermutationGenerator(Integer[] inputList) {
    this.inputList = inputList;
    this.interList = new Integer[inputList.length];
    this.size = inputList.length;
  }

  public Integer[] smallestLargerNumbers() {
    // out of bound values to track largest/smallest seen
    int smallestSeen = this.size;
    int largestSeen = -1;
    int smallestExisting = 0;
    int largestExisting = this.size - 1;
    Integer[] li = this.inputList; // reference

    // inverse arr [value] = index
    Integer[] inv = new Integer[this.size];

    // fill inverse array O(n)
    for (int i = 0; i < this.size; i++) {
      inv[li[i]] = i;
    }

    // last one is always null
    this.interList[this.size - 1] = null;

    // iterate up until last index
    for (int i = 0; i < this.size - 1; i++) {
      int curr = li[i];
      if (curr < largestExisting && curr > largestSeen) {
        largestSeen = curr;
        if (curr < smallestSeen) smallestSeen = curr;
        curr++;
        if (inv[curr] > i) {}
      }
    }

    return this.interList;
  }

  public Integer[] largestSmallerNumbers() {
    return new Integer[0];
  }

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

  public Integer[] getInputList() {
    return this.inputList;
  }

  public void printInputList() {
    String list = "Input List: [";
    list += this.inputList[0];
    for (int i = 1; i < this.size; i++) {
      list += "," + this.inputList[i];
    }
    list += "]";
    System.out.println(list);
  }

  public void printInterList() {
    String list = "Inter List: [";
    list += this.interList[0];
    for (int i = 1; i < this.size; i++) {
      list += "," + this.interList[i];
    }
    list += "]";
    System.out.println(list);
  }
}

public class ListSection {
  public static void main(String[] args) {
    Integer[] arrTest = {8, 1, 2, 0, 7, 4, 3, 9, 5, 6};
    PermutationGenerator gen = new PermutationGenerator(arrTest);
    gen.printInputList();
    // PermutationGenerator gen = new PermutationGenerator(10);
    // gen.printInputList();
    // gen.printInterList();
    // gen.shuffle();
    // gen.printInterList();
  }
}
