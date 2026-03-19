import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shakespeare {
  static final String filePath = "shakespeare.txt";
  public String text;
  public String[] lines;

  public static void main(String[] args) {
    Shakespeare runner = new Shakespeare();

    System.out.println("\n---Test builtin map implementation---\n");
    BuiltinMap builtin = runner.new BuiltinMap(10);
    time(builtin::countWords, "countwords");
    time(builtin::mostCommonWords, "mostCommonWords");

    System.out.println("\n---Test custom map implementation---\n");
    CustomMap custom = runner.new CustomMap(10);
    time(custom::countWords, "countwords");
    time(custom::mostCommonWords, "mostCommonWords");
  }

  public static void time(Runnable func, String name) {
    Instant start = Instant.now();
    long startTime = start.getEpochSecond() * 1_000_000 + start.getNano() / 1_000;

    func.run();

    Instant end = Instant.now();
    long endTime = end.getEpochSecond() * 1_000_000 + end.getNano() / 1_000;

    long runtime = endTime - startTime;
    System.out.println("" + name + " runtime: " + runtime + " microseconds\n");
  }

  public Shakespeare() {
    try {
      text = new String(Files.readAllBytes(Paths.get(getClass().getResource(filePath).toURI())));
      System.out.println("File read successfully");
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
      System.out.println("File read failed");
      text = "";
    }
    lines = text.split("\n");
  }

  class CustomMap {
    public int top_n;
    public CustomHashTable wordCounts;

    public CustomMap(int n) {
      top_n = n;
      wordCounts = new CustomHashTable(50000);
    }

    public void countWords() {
      for (String word : text.split(" ")) {
        wordCounts.put(word);
      }
    }

    public void mostCommonWords() {
      CustomLinkedList list = new CustomLinkedList(wordCounts.list.get(0), top_n);
      for (int i = 1; i < wordCounts.list.size(); i++) {
        list.insert(wordCounts.list.get(i));
      }

      list.print();
    }

    class CustomLinkedList {
      Node start = null; // should contain the largest element
      Node end = null;
      int lowest;
      int highest;
      int size;
      int maxSize;

      public CustomLinkedList(Pair<String, Integer> data, int maxSize) {
        Node temp = new Node(data);

        start = temp;
        end = temp;
        lowest = temp.data.value;
        highest = temp.data.value;
        size = 1;
        this.maxSize = maxSize;
      }

      public void print() {
        Node curr = start;

        while (curr.next != null) {
          System.out.println(curr.data.key + " : " + curr.data.value);
          curr = curr.next;
        }

        System.out.println(curr.data.key + " : " + curr.data.value);
        curr = curr.next;
      }

      public void insert(Pair<String, Integer> data) {
        Node temp = new Node(data);
        if (data.value > highest) { // new start
          temp.next = start;
          start.prev = temp;
          start = temp;
          highest = data.value;
          if (size == maxSize) { // remove smallest
            end = end.prev;
            end.next = null;
            lowest = end.data.value;
          } else {
            size++;
          }
          return;
        }
        if (data.value <= lowest) {
          if (size == maxSize) { // don't need more elements
            return;
          }
          temp.prev = end;
          end.next = temp;
          end = temp;
          lowest = data.value;
          size++;
          return;
        }

        Node curr = start;

        // find node pair that this new one sits between
        while (curr.next != null && curr.next.data.value >= data.value) {
          curr = curr.next;
        }

        temp.prev = curr;
        temp.next = curr.next;
        if (curr.next != null) curr.next.prev = temp;
        curr.next = temp;

        if (size == maxSize) {
          end = end.prev;
          end.next = null;
          lowest = end.data.value;
        } else {
          size++;
        }
      }
    }

    // custom node class for this linked list
    class Node {
      public Pair<String, Integer> data;
      public Node next;
      public Node prev;

      public Node(Pair<String, Integer> data) {
        this.data = data;
        this.next = null;
        this.prev = null;
      }
    }

    class CustomHashTable {
      private String[] keys;
      private Pair<String, Integer>[] entries;
      private int capacity;
      public ArrayList<Pair<String, Integer>> list;

      @SuppressWarnings("unchecked")
      public CustomHashTable(int size) {
        capacity = nextPrime(size * 2);
        keys = new String[capacity];
        entries = new Pair[capacity];
        list = new ArrayList<>(size);
      }

      public void put(String key) {
        int index = Math.abs(key.hashCode()) % capacity;

        while (keys[index] != null) {
          if (keys[index].equals(key)) {
            entries[index].value++;
            return;
          }
          index = (index + 1) % capacity;
        }

        Pair<String, Integer> p = new Pair<>(key, 1);
        keys[index] = key;
        entries[index] = p;
        list.add(p);
      }

      private int nextPrime(int n) {
        while (!isPrime(n)) n++;
        return n;
      }

      private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
          if (n % i == 0) return false;
        }
        return true;
      }
    }
  }

  /** Implementation provided by instructor */
  class BuiltinMap {
    public int top_n;
    public Map<String, Integer> wordCounts;

    public BuiltinMap(int n) {
      top_n = n;
    }

    public void countWords() {
      // What should we choose as capacity?
      wordCounts = new HashMap<>(50000);

      // for each string in the dataset, records the count
      for (String word : text.split(" ")) {
        if (wordCounts.containsKey(word)) {
          wordCounts.put(word, wordCounts.get(word) + 1);
        } else {
          wordCounts.put(word, 1);
        }
      }
    }

    public void mostCommonWords() {
      List<Pair<String, Integer>> sortedWordCounts = new ArrayList<>();
      wordCounts.entrySet().stream()
          .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
          .limit(top_n)
          .forEach(entry -> sortedWordCounts.add(new Pair<>(entry.getKey(), entry.getValue())));
      sortedWordCounts.forEach(System.out::println);
    }
  }

  static class Pair<K, V> {
    public K key;
    public V value;

    Pair(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public String toString() {
      return key + " : " + value;
    }
  }
}
