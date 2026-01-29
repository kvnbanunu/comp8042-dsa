class PermutationGenerator {
  private int size;
  private int[] inputList;
  private int[] interList;

  public PermutationGenerator(int n) {
    this.size = n;
    this.inputList = new int[n];
    this.interList = new int[n];

    for (int i = 0; i < n; i++) {
      this.inputList[i] = i;
    }
  }

  public PermutationGenerator(int[] inputList) {
    this.inputList = inputList;
    this.interList = new int[inputList.length];
    this.size = inputList.length;
  }

  public int[] smallestLargerNumbers() {
    return new int[0];
  }

  public int[] largestSmallerNumbers() {
    return new int[0];
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
    PermutationGenerator gen = new PermutationGenerator(10);
    gen.printInputList();
    gen.shuffle();
    gen.printInputList();
    gen.shuffle();
    gen.printInputList();
    gen.shuffle();
    gen.printInputList();
    gen.shuffle();
    gen.printInputList();
    gen.shuffle();
    gen.printInputList();
    gen.shuffle();
  }
}
