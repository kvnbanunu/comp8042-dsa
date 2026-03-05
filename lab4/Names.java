import java.util.LinkedList;
import java.util.List;

public class Names {
  public static void main(String[] args) {
    SeparateChainHashTable SCHT = new SeparateChainHashTable();

    long startTime = System.currentTimeMillis();

    long endTime = System.currentTimeMillis();

    System.out.println("Elapsed time: " + (endTime - startTime));
  }
}

class SeparateChainHashTable {

  private static final int DEFAULT_TABLE_SIZE = 101;
  public List<String>[] theLists;
  public int currentSize;

  public SeparateChainHashTable() {
    this(DEFAULT_TABLE_SIZE);
  }

  public SeparateChainHashTable(int size) {
    theLists = new LinkedList[nextPrime(size)];
    for (int i = 0; i < theLists.length; i++) {
      theLists[i] = new LinkedList<>();
    }
  }

  public void insert(String x) {
    List<String> whichList = theLists[myhash(x)];
    if (!whichList.contains(x)) {
      whichList.add(x);
      if (++currentSize > theLists.length) rehash();
    }
  }

  public static int hash(String key, int tableSize) {
    int hashVal = 0;

    for (int i = 0; i < key.length(); i++) {
      hashVal = 37 * hashVal + key.charAt(i);

      hashVal %= tableSize;
      if (hashVal < 0) {
        hashVal += tableSize;
      }
    }
    return hashVal;
  }

  public void makeEmpty() {
    for (int i = 0; i < theLists.length; i++) theLists[i].clear();
    currentSize = 0;
  }

  private int myhash(String x) {
    int hashVal = x.hashCode();

    hashVal %= theLists.length;
    if (hashVal < 0) hashVal += theLists.length;

    return hashVal;
  }

  private void rehash() {
    List<String>[] oldLists = theLists;

    // Create new double-sized, empty table
    theLists = new LinkedList[nextPrime(2 * theLists.length)];
    for (int j = 0; j < theLists.length; j++) theLists[j] = new LinkedList<>();

    // Copy table over
    currentSize = 0;
    for (List<String> list : oldLists) for (String item : list) insert(item);
  }

  private static boolean isPrime(int n) {
    if (n == 2 || n == 3) return true;

    if (n == 1 || n % 2 == 0) return false;

    for (int i = 3; i * i <= n; i += 2) if (n % i == 0) return false;

    return true;
  }

  private static int nextPrime(int n) {
    if (n % 2 == 0) n++;

    for (; !isPrime(n); n += 2)
      ;

    return n;
  }
}

class QuadraticProbingHashTable {

  private static final int DEFAULT_TABLE_SIZE = 101;

  private HashEntry[] array; // The array of elements
  private int occupied; // The number of occupied cells
  private int theSize; // Current size

  /** Construct the hash table. */
  public QuadraticProbingHashTable() {
    this(DEFAULT_TABLE_SIZE);
  }

  /**
   * Construct the hash table.
   *
   * @param size the approximate initial size.
   */
  public QuadraticProbingHashTable(int size) {
    allocateArray(size);
    doClear();
  }

  /**
   * Insert into the hash table. If the item is already present, do nothing.
   *
   * @param x the item to insert.
   */
  public boolean insert(String x) {
    // Insert x as active
    int currentPos = findPos(x);
    if (isActive(currentPos)) return false;

    array[currentPos] = new HashEntry(x, true);
    theSize++;

    // Rehash; see Section 5.5
    if (++occupied > array.length / 2) rehash();

    return true;
  }

  /** Expand the hash table. */
  private void rehash() {
    HashEntry[] oldArray = array;

    // Create a new double-sized, empty table
    allocateArray(2 * oldArray.length);
    occupied = 0;
    theSize = 0;

    // Copy table over
    for (HashEntry entry : oldArray) if (entry != null && entry.isActive) insert(entry.element);
  }

  /**
   * Method that performs quadratic probing resolution.
   *
   * @param x the item to search for.
   * @return the position where the search terminates.
   */
  private int findPos(String x) {
    int offset = 1;
    int currentPos = myhash(x);

    while (array[currentPos] != null && !array[currentPos].element.equals(x)) {
      currentPos += offset; // Compute ith probe
      offset += 2;
      if (currentPos >= array.length) currentPos -= array.length;
    }

    return currentPos;
  }

  /**
   * Remove from the hash table.
   *
   * @param x the item to remove.
   * @return true if item removed
   */
  public boolean remove(String x) {
    int currentPos = findPos(x);
    if (isActive(currentPos)) {
      array[currentPos].isActive = false;
      theSize--;
      return true;
    } else return false;
  }

  /**
   * Get current size.
   *
   * @return the size.
   */
  public int size() {
    return theSize;
  }

  /**
   * Get length of internal table.
   *
   * @return the size.
   */
  public int capacity() {
    return array.length;
  }

  /**
   * Find an item in the hash table.
   *
   * @param x the item to search for.
   * @return the matching item.
   */
  public boolean contains(String x) {
    int currentPos = findPos(x);
    return isActive(currentPos);
  }

  /**
   * Return true if currentPos exists and is active.
   *
   * @param currentPos the result of a call to findPos.
   * @return true if currentPos is active.
   */
  private boolean isActive(int currentPos) {
    return array[currentPos] != null && array[currentPos].isActive;
  }

  /** Make the hash table logically empty. */
  public void makeEmpty() {
    doClear();
  }

  private void doClear() {
    occupied = 0;
    for (int i = 0; i < array.length; i++) array[i] = null;
  }

  private int myhash(String x) {
    int hashVal = x.hashCode();

    hashVal %= array.length;
    if (hashVal < 0) hashVal += array.length;

    return hashVal;
  }

  private static class HashEntry {
    public String element; // the element
    public boolean isActive; // false if marked deleted

    public HashEntry(String e) {
      this(e, true);
    }

    public HashEntry(String e, boolean i) {
      element = e;
      isActive = i;
    }
  }

  /**
   * Internal method to allocate array.
   *
   * @param arraySize the size of the array.
   */
  private void allocateArray(int arraySize) {
    array = new HashEntry[nextPrime(arraySize)];
  }

  /**
   * Internal method to find a prime number at least as large as n.
   *
   * @param n the starting number (must be positive).
   * @return a prime number larger than or equal to n.
   */
  private static int nextPrime(int n) {
    if (n % 2 == 0) n++;

    for (; !isPrime(n); n += 2)
      ;

    return n;
  }

  /**
   * Internal method to test if a number is prime. Not an efficient algorithm.
   *
   * @param n the number to test.
   * @return the result of the test.
   */
  private static boolean isPrime(int n) {
    if (n == 2 || n == 3) return true;

    if (n == 1 || n % 2 == 0) return false;

    for (int i = 3; i * i <= n; i += 2) if (n % i == 0) return false;

    return true;
  }
}
