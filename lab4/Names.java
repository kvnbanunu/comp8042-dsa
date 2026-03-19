import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Names {
  public static void main(String[] args) {
    File namesFile = new File("first.txt");
    int namesSize = 19948;
    int size = nextPrime(namesSize / 2 + namesSize);
    String[] names = new String[namesSize];
    int index = 0;

    try (Scanner scan = new Scanner(namesFile)) {
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        names[index++] = line;
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }

    String[] toQuery = {
      "Abdel",
      "Almerinda",
      "Astor",
      "Bridgette",
      "Cosmina",
      "Driss",
      "Evgenia",
      "Girolamo",
      "Huber",
      "Jayro",
      "Ken",
      "Liqin",
      "Marisca",
      "Muhammed",
      "Olinda",
      "Ratiba",
      "Sarama",
      "Sthefany",
      "Vaidas",
      "Yamile",
      "Zygmunt"
    };

    Hasher SCHT = new SeparateChainHashTable(size);
    SCHT.runTests(names, toQuery);
    Hasher QPHT = new QuadraticProbingHashTable(size);
    QPHT.runTests(names, toQuery);
    Hasher DHT = new DoubleHashTable(size);
    DHT.runTests(names, toQuery);
    Hasher PHT = new PerfectHashTable(size);
    PHT.runTests(names, toQuery);
  }

  public static int nextPrime(int n) {
    if (n % 2 == 0) n++;

    for (; !isPrime(n); n += 2)
      ;

    return n;
  }

  private static boolean isPrime(int n) {
    if (n == 2 || n == 3) return true;

    if (n == 1 || n % 2 == 0) return false;

    for (int i = 3; i * i <= n; i += 2) if (n % i == 0) return false;

    return true;
  }
}

/*
 * Abstract class for hashing
 * Includes methods to run tests and print stats
 */
abstract class Hasher {
  String title;
  int numInsertions = 0;
  int totalCollisions = 0;

  public abstract int insert(String x);

  public abstract void query(String x);

  public abstract int getTableSize();

  public abstract int getOccupied();

  public void getAverageCollisions() {
    double avg = (double) (totalCollisions) / (double) (numInsertions) * 100;
    System.out.printf("Average Collisions: %.2f", avg);
    System.out.println("%");
  }

  public void getTotalCollisions() {
    System.out.println("Total Collisions: " + totalCollisions);
  }

  public void getEmptySlots() {
    int empty = getTableSize() - getOccupied();
    System.out.println("Empty Slots: " + empty);
  }

  public void getLoadFactor() {
    double load = (double) (getOccupied()) / (double) (getTableSize()) * 100;
    System.out.printf("Load Factor: %.2f", load);
    System.out.println("%");
  }

  public void queryNames(String[] names) {
    System.out.println("Querying names");
    Instant startTime = Instant.now();

    for (String n : names) {
      query(n);
    }

    Instant endTime = Instant.now();
    System.out.println(
        "Elapsed time: "
            + ((endTime.getEpochSecond() * 1_000_000 + endTime.getNano() / 1_000)
                - (startTime.getEpochSecond() * 1_000_000 + startTime.getNano() / 1_000))
            + " microseconds");
  }

  public void Insert(String x) {
    totalCollisions += insert(x);
    numInsertions++;
  }

  public void runTests(String[] names, String[] toQuery) {
    System.out.println("Starting tests for " + title);
    System.out.println("Inserting names");
    for (String name : names) {
      Insert(name);
    }
    System.out.println("Inserting complete");
    getAverageCollisions();
    getTotalCollisions();
    System.out.println("Table Size: " + getTableSize());
    System.out.println("Slots Occupied: " + getOccupied());
    getEmptySlots();
    getLoadFactor();
    queryNames(toQuery);
    System.out.println("Completed tests for " + title);
  }
}

class DoubleHashTable extends Hasher {
  private int tableSize;
  private int occupied;
  private String[] list;

  public DoubleHashTable(int size) {
    tableSize = size;
    list = new String[size];
    occupied = 0;
    title = "Double Hashing Table";
  }

  @Override
  public int insert(String x) {
    int hashCode = x.hashCode();
    hashCode %= tableSize;
    if (hashCode < 0) {
      hashCode += tableSize;
    }
    int base = primaryHash(hashCode);
    int step = secondaryHash(hashCode);
    int collisions = 0;

    base = (base + step) % tableSize;

    int probe = base;

    while (list[probe] != null) {
      probe = (probe + step) % tableSize;
      collisions++;

      // detect cycle
      if (probe == base) {
        System.out.println("Failed to insert " + x);
        return collisions;
      }
    }

    list[probe] = x;
    occupied++;
    return collisions;
  }

  @Override
  public void query(String x) {
    int hashCode = x.hashCode();
    hashCode %= tableSize;
    if (hashCode < 0) {
      hashCode += tableSize;
    }
    int base = primaryHash(hashCode);
    int step = secondaryHash(hashCode);

    base = (base + step) % tableSize;

    int probe = base;

    while (!list[probe].equals(x)) {
      probe = (probe + step) % tableSize;

      // detect cycle
      if (probe == base) {
        System.out.println("Failed to find " + x);
        return;
      }
    }

    System.out.println("Found: " + x + " Index: " + probe);
  }

  @Override
  public int getTableSize() {
    return tableSize;
  }

  @Override
  public int getOccupied() {
    return occupied;
  }

  private int primaryHash(int hashCode) {
    return hashCode % tableSize;
  }

  private int secondaryHash(int hashCode) {
    return 97 - (hashCode % 97);
  }
}

class PerfectHashTable extends Hasher {
  private int tableSize;
  private int occupied;
  private String[] list;
  private int maxHashes;
  private int[][] hashes;
  private int hashCount;
  private int currentHash;
  private int prime;

  public PerfectHashTable(int size) {
    title = "Perfect Hashing Table";
    tableSize = size;
    list = new String[size];
    occupied = 0;
    maxHashes = 20;
    hashes = new int[maxHashes][2];
    hashCount = 0;
    currentHash = -1;
    prime = Names.nextPrime(size);
  }

  private int[] nextHash() {
    currentHash++;
    if (hashCount < maxHashes) {

      int a = (int) (Math.random() * tableSize) + 1;
      int b = (int) (Math.random() * tableSize) + 1;

      boolean exists = false;
      while (true) {
        for (int i = 0; i < hashCount; i++) {
          if (hashes[i][0] == a && hashes[i][1] == b) {
            exists = true;
            break;
          }
        }
        if (exists) {
          a = (int) (Math.random() * tableSize) + 1;
          b = (int) (Math.random() * tableSize) + 1;
          exists = false;
        } else {
          break;
        }
      }

      hashCount++;
      hashes[currentHash][0] = a;
      hashes[currentHash][1] = b;
      return hashes[currentHash];
    }

    currentHash %= maxHashes;
    return hashes[currentHash];
  }

  private int hash(String x, int a, int b) {
    int hashed = x.hashCode();
    hashed %= tableSize;
    if (hashed < 0) {
      hashed += tableSize;
    }
    return ((a * hashed + b) % prime) % tableSize;
  }

  @Override
  public int insert(String x) {
    int[] hash;
    if (hashCount == 0) {
      hash = nextHash();
    } else {
      hash = hashes[currentHash];
    }
    int a = hash[0];
    int b = hash[1];

    int hashed = hash(x, a, b);
    int collisions = 0;

    while (list[hashed] != null) {
      collisions++;

      // cycle
      if (collisions == maxHashes) {
        System.out.println("Failed to insert " + x);
        return collisions;
      }

      hash = nextHash();
      a = hash[0];
      b = hash[1];
      hashed = hash(x, a, b);
    }

    list[hashed] = x;
    occupied++;

    return collisions;
  }

  @Override
  public void query(String x) {
    int a = hashes[currentHash][0];
    int b = hashes[currentHash][1];

    int hashed = hash(x, a, b);
    int tries = 0;

    while (list[hashed] == null || (list[hashed] != null && !list[hashed].equals(x))) {
      tries++;

      // cycle
      if (tries == maxHashes) {
        System.out.println("Failed to find " + x);
        return;
      }

      int[] next = nextHash();
      a = next[0];
      b = next[1];
      hashed = hash(x, a, b);
    }

    System.out.println("Found: " + x + " Index: " + hashed);
  }

  @Override
  public int getTableSize() {
    return tableSize;
  }

  @Override
  public int getOccupied() {
    return occupied;
  }
}

/** Provided by instructor */
class SeparateChainHashTable extends Hasher {

  private static final int DEFAULT_TABLE_SIZE = 101;
  public List<String>[] theLists;
  public int currentSize;

  public SeparateChainHashTable() {
    this(DEFAULT_TABLE_SIZE);
  }

  public SeparateChainHashTable(int size) {
    title = "Separate Chain Hash Table";
    theLists = new LinkedList[nextPrime(size)];
    for (int i = 0; i < theLists.length; i++) {
      theLists[i] = new LinkedList<>();
    }
  }

  @Override
  public void query(String x) {
    int hashed = myhash(x);
    List<String> whichList = theLists[hashed];
    int position = 0;
    for (String s : whichList) {
      if (x.equals(s)) {
        System.out.println("Found " + x + " Index: " + hashed + " Position: " + position);
        return;
      }
      position++;
    }
    System.out.println(x + " not found.");
  }

  @Override
  public int getTableSize() {
    return theLists.length;
  }

  @Override
  public int getOccupied() {
    int numOccupied = 0;
    for (List<String> l : theLists) {
      if (l.size() > 0) numOccupied++;
    }
    return numOccupied;
  }

  @Override
  public int insert(String x) {
    int hashed = myhash(x);
    int collisions = 0;
    List<String> whichList = theLists[hashed];
    if (whichList.size() > 0) {
      collisions++;
    }
    if (!whichList.contains(x)) {
      whichList.add(x);
      if (++currentSize > theLists.length) rehash();
    }
    return collisions;
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

class QuadraticProbingHashTable extends Hasher {

  private static final int DEFAULT_TABLE_SIZE = 101;

  private HashEntry[] array; // The array of elements
  private int occupied; // The number of occupied cells
  private int theSize; // Current size
  private int localCollisions = 0;

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
    title = "Quadratic Probing Hash Table";
    allocateArray(size);
    doClear();
  }

  @Override
  public void query(String x) {
    int found = findPos(x);
    System.out.println("Found: " + x + " Index: " + found);
  }

  @Override
  public int getTableSize() {
    return array.length;
  }

  @Override
  public int getOccupied() {
    return occupied;
  }

  /**
   * Insert into the hash table. If the item is already present, do nothing.
   *
   * @param x the item to insert.
   */
  @Override
  public int insert(String x) {
    // Insert x as active
    int currentPos = findPos(x);
    if (isActive(currentPos)) return 0;

    array[currentPos] = new HashEntry(x, true);
    theSize++;

    // Rehash; see Section 5.5
    if (++occupied > array.length / 2) rehash();

    return localCollisions;
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
    localCollisions = 0;

    while (array[currentPos] != null && !array[currentPos].element.equals(x)) {
      currentPos += offset; // Compute ith probe
      offset += 2;
      if (currentPos >= array.length) currentPos -= array.length;

      localCollisions++;
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
