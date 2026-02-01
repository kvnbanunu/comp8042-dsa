class PermutationGenerator {
  private int size;
  private Integer[] inputList;
  private Integer[] interList;

  // Initialize input and inter list to an array of size n
  // inputList is then filled with values 0..n-1
  // interList is filled with null values
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

  /**
   * returns an array of size n where each index i contains the smallest number that is larger than
   * inputList[i] that is stored in the index past i within the input array, otherwise null.
   * inputList is left unchanged, while interList contains the output and also returned.
   */
  public Integer[] smallestLargerNumbers() {
    int n = this.size;
    int smallestExisting = 0;
    int largestExisting = n - 1;
    Integer[] li = this.inputList; // reference to input list
    Integer[] res = this.interList; // inter / output list

    // inverse arr [value] = index
    Integer[] inv = new Integer[n];

    // fill inverse array O(n)
    for (int i = 0; i < n; i++) {
      inv[li[i]] = i;
    }

    // last one is always null
    res[n - 1] = null;

    // iterate up until second last index
    for (int i = 0; i < n - 1; i++) {
      int curr = li[i];

      if (curr == smallestExisting) {
        // find next smallest that exists after this number
        while (inv[smallestExisting] <= i) {
          smallestExisting++;
        }
        res[i] = smallestExisting;
      } else if (curr == largestExisting) {
        // find next largest that exists after this number
        while (inv[largestExisting] <= i) {
          largestExisting--;
        }
        // since largestExisting, nothing on the right side is larger => null
        res[i] = null;
      } else {
        // increment curr until it resides on the right side
        while (inv[curr] <= i) {
          curr++;
        }
        res[i] = curr;
      }
    }
    return res;
  }

  /**
   * returns an array of size n where each index i contains the largest number that is smaller than
   * inputList[i] that is stored in the index past i within the input array, otherwise null.
   * inputList is left unchanged, while interList contains the output and also returned.
   */
  public Integer[] largestSmallerNumbers() {
    int n = this.size;
    int smallestExisting = 0;
    int largestExisting = n - 1;
    Integer[] li = this.inputList; // reference to input list
    Integer[] res = this.interList; // inter / output list

    // inverse arr [value] = index
    Integer[] inv = new Integer[n];

    // fill inverse array O(n)
    for (int i = 0; i < n; i++) {
      inv[li[i]] = i;
    }

    // last one is always null
    res[n - 1] = null;

    for (int i = 0; i < n - 1; i++) {
      int curr = li[i];

      // do the same as smallestLarger, except flip results
      if (curr == smallestExisting) {
        while (inv[smallestExisting] <= i) {
          smallestExisting++;
        }
        res[i] = null;
      } else if (curr == largestExisting) {
        while (inv[largestExisting] <= i) {
          largestExisting--;
        }
        res[i] = largestExisting;
      } else {
        while (inv[curr] <= i) {
          curr--;
        }
        res[i] = curr;
      }
    }
    return res;
  }

  // shuffles the input array by randomly swapping values storing the final result in interList.
  // Both inputList and interList are mutated.
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

  public Integer[] getInputList() {
    return this.inputList;
  }

  public Integer[] getInterList() {
    return this.interList;
  }

  public void printList(String title, Integer[] li) {
    String list = title + ": [";

    list += li[0];
    for (int i = 1; i < li.length; i++) {
      list += "," + li[i];
    }
    list += "]";
    System.out.println(list);
  }

  public void printInputList() {
    printList("Input", this.inputList);
  }

  public void printInterList() {
    printList("Inter", this.interList);
  }

  public void printOutputList(Integer[] li) {
    printList("Output", li);
  }
}

public class ListSection {
  public static void main(String[] args) {
    // Integer[] arrTest = {8, 1, 2, 0, 7, 4, 3, 9, 5, 6};
    Integer[] arrTest = {1, 0, 6, 4, 2, 3, 5}; // lab example
    PermutationGenerator gen = new PermutationGenerator(arrTest);
    gen.printInputList();
    Integer[] small = gen.smallestLargerNumbers();
    gen.printOutputList(small); // [2,2,null,5,3,5,null]
    Integer[] large = gen.largestSmallerNumbers();
    gen.printOutputList(large); // [0,null,5,3,null,null,null]
    // PermutationGenerator gen = new PermutationGenerator(10);
    // gen.printInputList();
    // gen.printInterList();
    // gen.shuffle();
    // gen.printInterList();
  }
}
