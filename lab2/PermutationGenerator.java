class PermutationGenerator {
  private int size;
  private int[] inputList;
  private int[] interList;

  public PermutationGenerator(int n) {
    this.size = n;
    this.inputList = new int[n];
    this.interList = new int[n];
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

  public void shuffle() {}
}
