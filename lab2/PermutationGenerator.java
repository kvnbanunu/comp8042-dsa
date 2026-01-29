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
      int rand = (int) Math.random() * this.size;
      this.interList[i] = this.inputList[rand];
      this.interList[rand] = this.inputList[i];
    }
  }
}
