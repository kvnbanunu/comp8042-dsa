/*
 * BCIT COMP 8042
 * Lab Assignment 3
 * Social Network
 * Kevin Nguyen
 * A00955925
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

public class FriendGraph {
  private Map<String, Set<String>> adjacencyList;
  private ContactList cl;

  public FriendGraph() {
    adjacencyList = new HashMap<>();
    cl = new ContactList();
  }

  public void addContact(String c) {
    if (c == null) return;
    adjacencyList.putIfAbsent(c, new LinkedHashSet<>());
    cl.insertContact(c);
  }

  public void removeContact(String name) {
    String found = cl.findContact(name);
    if (found == null) {
      return;
    }

    // remove contact from everyones friends list
    Set<String> friends = adjacencyList.getOrDefault(found, Collections.emptySet());
    for (String f : friends) {
      adjacencyList.get(f).remove(found);
    }

    // remove the contact
    adjacencyList.remove(found);
    cl.removeContact(name);
  }

  public void addFriend(String name1, String name2) {
    String c1 = cl.findContact(name1);
    String c2 = cl.findContact(name2);

    if (c1 == null || c2 == null) {
      // one of the contacts not found
      return;
    }

    adjacencyList.get(c1).add(c2);
    adjacencyList.get(c2).add(c1);
  }

  public void removeFriend(String name1, String name2) {
    String c1 = cl.findContact(name1);
    String c2 = cl.findContact(name2);

    if (c1 == null || c2 == null) {
      return;
    }

    boolean removed1 = adjacencyList.getOrDefault(c1, Collections.emptySet()).remove(c2);
    boolean removed2 = adjacencyList.getOrDefault(c2, Collections.emptySet()).remove(c1);

    if (!removed1 && !removed2) {
      System.out.println(
          "Contacts '" + name1 + "' and '" + name2 + "' were not friends to begin with");
    }
  }

  public void printFriends(String name) {
    String found = cl.findContact(name);
    if (found == null) {
      return;
    }

    Set<String> friends = adjacencyList.get(found);
    if (friends.isEmpty()) {
      System.out.println("Contact: '" + name + "' has no friends");
      return;
    }

    System.out.println("Friends list for '" + name + "':");
    for (String f : friends) {
      System.out.println("\t" + f);
    }
  }

  public void printMostFriendsOfFriends() {
    if (adjacencyList.isEmpty()) {
      System.out.println("Graph is empty");
      return;
    }

    String best = null;
    int highest = -1;

    for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
      String c = entry.getKey();
      Set<String> firstDegree = entry.getValue();

      Set<String> fof = new HashSet<>();
      for (String f : firstDegree) {
        fof.addAll(adjacencyList.get(f));
      }

      fof.remove(c);
      fof.removeAll(firstDegree);

      int count = fof.size();
      if (count > highest) {
        highest = count;
        best = c;
      }
    }

    if (best == null || highest == 0) {
      System.out.println("No friends of friends found");
      return;
    }

    System.out.println(
        "Contact with the most friends of friends: '" + best + "' | " + highest + " friends");
  }

  public void printShortestPath(String startName, String endName) {
    String start = cl.findContact(startName);
    String end = cl.findContact(endName);

    if (start == null || end == null) {
      return;
    }

    if (start.equals(end)) {
      System.out.println("Error: " + startName + " == " + endName);
      return;
    }

    Queue<String> q = new LinkedList<>();
    Map<String, String> from = new HashMap<>();

    q.add(start);
    from.put(start, null);

    boolean found = false;

    while (!q.isEmpty()) {
      String curr = q.poll();

      if (curr.equals(end)) {
        found = true;
        break;
      }

      for (String n : adjacencyList.get(curr)) {
        if (!from.containsKey(n)) {
          from.put(n, curr);
          q.add(n);
        }
      }
    }

    if (!found) {
      System.out.println("No connection between '" + startName + "' & '" + endName + "'");
      return;
    }

    // walk back from end
    List<String> path = new ArrayList<>();
    String next = end;
    while (next != null) {
      path.add(next);
      next = from.get(next);
    }

    // directly friends
    if (path.size() == 2) {
      System.out.println(startName + " is friends with " + endName);
      return;
    }

    String res = startName;
    for (int i = path.size() - 2; i >= 0; i--) {
      res += " -> " + path.get(i);
    }
    System.out.println(res);
  }

  public static void main(String[] args) {
    FriendGraph G = new FriendGraph();
    // ommitted last names for better flow
    G.addContact("Victor");
    G.addContact("Lucas");
    G.addContact("Josh");
    G.addContact("Timur");
    G.addContact("Louise");
    G.addContact("Joe");
    G.addContact("Brian");
    G.addContact("Sepehr");
    G.addContact("Wilson");
    G.addContact("Kohei");
    G.addContact("Evin");
    G.addContact("Kevin");
    G.addContact("Nathan");
    G.addContact("Madhav");
    G.addContact("Raziel");
    G.addContact("John");
    G.addContact("Clay");
    G.addContact("Fonse");
    G.addContact("Nicholas");
    G.addContact("Reese");
    G.addContact("Keagan");
    G.addContact("Seung Jae");
    G.addContact("Brandon");
    G.addContact("Blaise");

    G.addFriend("Kevin", "Evin");
    G.addFriend("Kevin", "Lucas");
    G.addFriend("Evin", "Lucas");
    G.addFriend("Lucas", "Sepehr");
    G.addFriend("Sepehr", "Evin");
    G.addFriend("Lucas", "Reese");

    G.printFriends("Kevin");
    G.printFriends("Sepehr");

    G.printMostFriendsOfFriends();

    System.out.println("\nShortest path between Kevin and Sepehr");
    G.printShortestPath("Kevin", "Sepehr");

    System.out.println("\nRemove Evin from Sepehr");
    G.removeFriend("Evin", "Sepehr");

    System.out.println("\nShortest path between Kevin and Sepehr");
    G.printShortestPath("Kevin", "Sepehr");
  }
}

/***********************************************
 * Slightly altered ContactList from section 2 *
 * Contact is simplified to just a String name *
 ***********************************************/

class ContactList {
  private AvlTree<String> contacts;
  private ArrayList<String> inOrder;
  private int size;

  public ContactList() {
    contacts = new AvlTree<String>();
    size = 0;
  }

  public void insertContact(String c) {
    contacts.insert(c);
    size++;
  }

  public String findContact(String name) {
    String found = findContact(name, contacts.getRoot());
    if (found == null) {
      System.out.println("Contact: '" + name + "' not found.");
    }
    return found;
  }

  // recursive helper for findContact
  private String findContact(String name, AvlTreeNode<String> root) {
    if (root == null) return null;

    int comparison = name.compareToIgnoreCase(root.getValue());

    if (comparison == 0) {
      return root.getValue();
    } else if (comparison < 0) {
      return findContact(name, root.getLeftChild());
    } else {
      return findContact(name, root.getRightChild());
    }
  }

  public void removeContact(String name) {
    String found = findContact(name);
    if (found == null) return;

    contacts.remove(found);
    size--;
    System.out.println("Removed from contact list - " + found);
  }

  // inOrderTraversal for AVLTree to help with the getList methods
  private void inOrderTraverse(AvlTreeNode<String> node) {
    if (node == null) return;
    inOrderTraverse(node.getLeftChild());
    inOrder.add(node.getValue());
    inOrderTraverse(node.getRightChild());
  }

  public List<String> getEveryContact() {
    if (inOrder != null && inOrder.size() == size) {
      return inOrder;
    }
    inOrder = new ArrayList<>(size);
    inOrderTraverse(contacts.getRoot());
    return inOrder;
  }

  public List<String> getEveryContactStartingWith(char letter) {
    if (inOrder == null || inOrder.size() != size) {
      inOrder = new ArrayList<>(size);
      inOrderTraverse(contacts.getRoot());
    }

    List<String> res = new ArrayList<>(size);

    boolean found = false;

    for (int i = 0; i < size; i++) {
      String c = inOrder.get(i);
      if (c.toLowerCase().charAt(0) == letter) {
        res.add(c);
        found = true;
      } else if (found) {
        break;
      }
    }

    return res;
  }

  public List<String> getStringMatchingContacts(String segment) {
    if (inOrder == null || inOrder.size() != size) {
      inOrder = new ArrayList<>(size);
      inOrderTraverse(contacts.getRoot());
    }

    List<String> list = new ArrayList<>(size);

    for (String c : inOrder) {
      if (contains(c, segment)) {
        list.add(c);
      }
    }
    return list;
  }

  private boolean contains(String s, String segment) {
    s = s.toLowerCase();
    segment = segment.toLowerCase();
    int i = 0;

    while (s.length() - i >= segment.length()) {
      if (s.charAt(i) == segment.charAt(0)) {
        int j = i;
        while (j - i < segment.length()) {
          if (s.charAt(j) != segment.charAt(j - i)) break;
          j++;
          if (j - i == segment.length()) return true;
        }
      }
      i++;
    }
    return false;
  }
}

/*******************************************************
 * The code below was provided for the lab assignment. *
 *******************************************************/

// Trees ----------------------------------------------------------------------

class GenericTree<T extends Comparable<? super T>> {
  private GenericTreeNode<T> root;
  private int size;

  public GenericTree(GenericTreeNode<T> root) {
    this.root = root;
    this.size = 1;
  }

  public GenericTree() {
    this.size = 0;
  }

  /*
   * A very inefficient way to search - sequentially by level.
   */
  public GenericTreeNode<T> searchNode(T element) {
    for (GenericTreeNode<T> node : levelOrderTraverse()) {
      if (node.getValue().equals(element)) {
        return node;
      }
    }
    return null;
  }

  public GenericTreeNode<T> getRoot() {
    return root;
  }

  public int size() {
    return size;
  }

  public int height() {
    // ToDo: Students should implement this
    return 0;
  }

  public void addChild(GenericTreeNode<T> parent, GenericTreeNode<T> child) {
    /*
     * Adds children from right to left order
     */
    if (parent == null) {
      if (root == null) {
        root = child;
        size++;
      } else {
        throw new Error("Cannot add child to null parent in a non-empty tree.");
      }
    } else {
      parent.addChild(child);
      size++;
    }
  }

  public boolean removeLeaf(GenericTreeNode<T> parent, GenericTreeNode<T> child) {
    if (parent == null) {
      throw new Error("Cannot remove child from a null parent.");
    }
    if (!child.isLeaf()) {
      throw new Error("Can only remove leaves from a tree.");
    }

    boolean child_removed = parent.removeChild(child);

    if (child_removed) {
      size--;
    }
    return child_removed;
  }

  public Iterable<GenericTreeNode<T>> levelOrderTraverse() {
    return new Iterable<GenericTreeNode<T>>() {
      @Override
      public Iterator<GenericTreeNode<T>> iterator() {
        return new LevelOrderTraverser<T, GenericTreeNode<T>>(root);
      }
    };
  }
}

@SuppressWarnings("unchecked")
abstract class AbstractBinarySearchTree<
    T extends Comparable<? super T>, N extends BinarySearchTreeNode<T>> {
  protected N root;

  public AbstractBinarySearchTree() {
    root = null;
  }

  public N getRoot() {
    return root;
  }

  public void insert(T x) {
    root = insert(x, root);
  }

  public void remove(T x) {
    root = remove(x, root);
  }

  public T findMin() {
    if (isEmpty()) throw new Error();
    return findMin(root).getValue();
  }

  public T findMax() {
    if (isEmpty()) throw new Error();
    return findMax(root).getValue();
  }

  public boolean contains(T x) {
    return contains(x, root);
  }

  public void makeEmpty() {
    root = null;
  }

  public boolean isEmpty() {
    return root == null;
  }

  public void printTree() {
    if (isEmpty()) System.out.println("Empty tree");
    else printTree(root);
  }

  protected abstract N insert(T x, N subtreeRoot);

  protected abstract N remove(T x, N subtreeRoot);

  protected N findMin(N t) {
    if (t == null) return null;
    else if (t.getLeftChild() == null) return t;
    return findMin((N) t.getLeftChild());
  }

  protected N findMax(N t) {
    if (t != null) while (t.getRightChild() != null) t = (N) t.getRightChild();
    return t;
  }

  protected boolean contains(T x, N t) {
    if (t == null) return false;

    int compareResult = x.compareTo(t.getValue());

    if (compareResult < 0) return contains(x, (N) t.getLeftChild());
    else if (compareResult > 0) return contains(x, (N) t.getRightChild());
    else return true; // Match
  }

  protected void printTree(N t) {
    if (t != null) {
      printTree((N) t.getLeftChild());
      System.out.println(t.getValue());
      printTree((N) t.getRightChild());
    }
  }

  public int height() {
    return height(root);
  }

  protected int height(N t) {
    if (t == null) return -1;
    else return 1 + Math.max(height((N) t.getLeftChild()), height((N) t.getRightChild()));
  }

  public Iterable<N> levelOrderTraverse() {
    return new Iterable<N>() {
      @Override
      public Iterator<N> iterator() {
        return new LevelOrderTraverser<T, N>(root);
      }
    };
  }

  public Iterable<N> preOrderTraverse() {
    return new Iterable<N>() {
      @Override
      public Iterator<N> iterator() {
        return new PreOrderTraverser<T, N>(root);
      }
    };
  }

  public Iterable<N> postOrderTraverse() {
    return new Iterable<N>() {
      @Override
      public Iterator<N> iterator() {
        return new PostOrderTraverser<T, N>(root);
      }
    };
  }
}

class BinarySearchTree<T extends Comparable<? super T>>
    extends AbstractBinarySearchTree<T, BinarySearchTreeNode<T>> {

  @Override
  protected BinarySearchTreeNode<T> insert(T x, BinarySearchTreeNode<T> subtreeRoot) {
    if (subtreeRoot == null) {
      return new BinarySearchTreeNode<>(x);
    }

    int compareResult = x.compareTo(subtreeRoot.getValue());

    if (compareResult < 0) {
      subtreeRoot.setLeftChild(insert(x, subtreeRoot.getLeftChild()));
    } else if (compareResult > 0) {
      subtreeRoot.setRightChild(insert(x, subtreeRoot.getRightChild()));
    } else {
      // Duplicate; do nothing
    }
    return subtreeRoot;
  }

  @Override
  protected BinarySearchTreeNode<T> remove(T x, BinarySearchTreeNode<T> root) {
    if (root == null) {
      return null; // Item not found; do nothing
    }

    int compareResult = x.compareTo(root.getValue());

    if (compareResult < 0) root.setLeftChild(remove(x, root.getLeftChild()));
    else if (compareResult > 0) root.setRightChild(remove(x, root.getRightChild()));
    else {
      if (root.isLeaf()) {
        return null;
      }

      if (root.getLeftChild() == null) {
        return root.getRightChild();
      }

      if (root.getRightChild() == null) {
        return root.getLeftChild();
      }
      // When both children are present
      BinarySearchTreeNode<T> successor = findMin(root.getRightChild());
      root.setValue(successor.getValue());
      root.setRightChild(remove(successor.getValue(), root.getRightChild()));
    }
    return root;
  }
}

class AvlTree<T extends Comparable<? super T>> extends AbstractBinarySearchTree<T, AvlTreeNode<T>> {

  @Override
  protected AvlTreeNode<T> insert(T x, AvlTreeNode<T> t) {
    if (t == null) return new AvlTreeNode<>(x, null, null);

    int compareResult = x.compareTo(t.getValue());

    if (compareResult < 0) t.setLeftChild(insert(x, (AvlTreeNode<T>) t.getLeftChild()));
    else if (compareResult > 0) t.setRightChild(insert(x, (AvlTreeNode<T>) t.getRightChild()));
    else
      ; // Duplicate; do nothing
    return balance(t);
  }

  @Override
  protected AvlTreeNode<T> remove(T x, AvlTreeNode<T> t) {
    if (t == null) return t; // Item not found; do nothing

    int compareResult = x.compareTo(t.getValue());

    if (compareResult < 0) t.setLeftChild(remove(x, t.getLeftChild()));
    else if (compareResult > 0) t.setRightChild(remove(x, t.getRightChild()));
    else if (t.getLeftChild() != null && t.getRightChild() != null) // Two children
    {
      t.setValue(findMin(t.getRightChild()).getValue());
      t.setRightChild(remove(t.getValue(), t.getRightChild()));
    } else t = (t.getLeftChild() != null) ? t.getLeftChild() : t.getRightChild();
    return balance(t);
  }

  private AvlTreeNode<T> balance(AvlTreeNode<T> node) {
    if (node == null) return null;

    if (height(node.getLeftChild()) - height(node.getRightChild()) > 1) {
      if (height(node.getLeftChild().getLeftChild())
          >= height(node.getLeftChild().getRightChild())) {
        node = rotateRight(node); // LL case
      } else {
        node = doubleRotateLeftRight(node); // LR case
      }
    } else if (height(node.getRightChild()) - height(node.getLeftChild()) > 1) {
      if (height(node.getRightChild().getRightChild())
          >= height(node.getRightChild().getLeftChild())) {
        node = rotateLeft(node); // RR case
      } else {
        node = doubleRotateRightLeft(node); // RL case
      }
    }

    node.setHeight(Math.max(height(node.getLeftChild()), height(node.getRightChild())) + 1);
    return node;
  }

  private AvlTreeNode<T> rotateRight(AvlTreeNode<T> node) {
    AvlTreeNode<T> newRoot = node.getLeftChild();
    node.setLeftChild(newRoot.getRightChild());
    newRoot.setRightChild(node);
    updateHeight(node);
    updateHeight(newRoot);
    return newRoot;
  }

  private AvlTreeNode<T> rotateLeft(AvlTreeNode<T> node) {
    AvlTreeNode<T> newRoot = node.getRightChild();
    node.setRightChild(newRoot.getLeftChild());
    newRoot.setLeftChild(node);
    updateHeight(node);
    updateHeight(newRoot);
    return newRoot;
  }

  private AvlTreeNode<T> doubleRotateLeftRight(AvlTreeNode<T> node) {
    node.setLeftChild(rotateLeft(node.getLeftChild()));
    return rotateRight(node);
  }

  private AvlTreeNode<T> doubleRotateRightLeft(AvlTreeNode<T> node) {
    node.setRightChild(rotateRight(node.getRightChild()));
    return rotateLeft(node);
  }

  private void updateHeight(AvlTreeNode<T> node) {
    if (node != null) {
      node.setHeight(Math.max(height(node.getLeftChild()), height(node.getRightChild())) + 1);
    }
  }
}

// Nodes ----------------------------------------------------------------------

abstract class TreeNode<T> {
  protected T value;
  protected int height;

  public T getValue() {
    return value;
  }

  public void setValue(T newValue) {
    this.value = newValue;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String toString() {
    return getValue().toString();
  }

  public abstract List<? extends TreeNode<T>> getChildren();

  public abstract boolean isLeaf();
}

class GenericTreeNode<T> extends TreeNode<T> {
  private List<GenericTreeNode<T>> children;

  public GenericTreeNode(T value) {
    this.value = value;

    // ToDo for students: Is using an arraylist a good idea here?
    this.children = new ArrayList<>();
  }

  public List<GenericTreeNode<T>> getChildren() {
    return children;
  }

  public void addChild(GenericTreeNode<T> child) {
    /*
     * Adds child from right to left order
     */
    children.add(child);
  }

  public boolean removeChild(GenericTreeNode<T> child) {
    return children.remove(child);
  }

  public boolean isLeaf() {
    return children.isEmpty();
  }
}

interface BinaryTreeNode<T> {
  public BinaryTreeNode<T> getRightChild();

  public BinaryTreeNode<T> getLeftChild();

  public void setRightChild(T right);

  public void setLeftChild(T left);

  public int getHeight();

  public void setHeight(int height);
}

class BinarySearchTreeNode<T extends Comparable<? super T>> extends TreeNode<T>
    implements BinaryTreeNode<BinarySearchTreeNode<T>> {
  private BinarySearchTreeNode<T> left;
  private BinarySearchTreeNode<T> right;

  public BinarySearchTreeNode(T value) {
    this.value = value;
    this.left = null;
    this.right = null;
  }

  public BinarySearchTreeNode<T> getLeftChild() {
    return left;
  }

  public BinarySearchTreeNode<T> getRightChild() {
    return right;
  }

  public void setRightChild(BinarySearchTreeNode<T> right) {
    if (right == null) {
      this.right = null;
      return;
    }

    int comp = right.getValue().compareTo(this.getValue());

    if (comp > 0) {
      this.right = right;
    } else {
      throw new RuntimeException("Right child must be greater than parent");
    }
  }

  public void setLeftChild(BinarySearchTreeNode<T> left) {
    if (left == null) {
      this.left = null;
      return;
    }

    int comp = left.getValue().compareTo(this.getValue());

    if (comp < 0) {
      this.left = left;
    } else {
      throw new RuntimeException("Left child must be less than parent");
    }
  }

  @Override
  public List<TreeNode<T>> getChildren() {
    List<TreeNode<T>> children = new ArrayList<>();
    if (left != null) {
      children.add(left);
    }
    if (right != null) {
      children.add(right);
    }
    return children;
  }

  public boolean isLeaf() {
    return left == null && right == null;
  }
}

class AvlTreeNode<T extends Comparable<? super T>> extends BinarySearchTreeNode<T> {
  private AvlTreeNode<T> left;
  private AvlTreeNode<T> right;
  private int height;

  public AvlTreeNode(T value) {
    this(value, null, null);
  }

  public AvlTreeNode(T value, AvlTreeNode<T> left, AvlTreeNode<T> right) {
    super(value);
    setLeftChild(left);
    setRightChild(right);
  }

  public AvlTreeNode<T> getLeftChild() {
    return left;
  }

  public AvlTreeNode<T> getRightChild() {
    return right;
  }

  public void setLeftChild(AvlTreeNode<T> left) {
    if (left == null) {
      this.left = null;
      return;
    }

    int comp = left.getValue().compareTo(this.getValue());

    if (comp < 0) {
      this.left = left;
      return;
    } else {
      throw new RuntimeException("Left child must be less than parent");
    }
  }

  public void setRightChild(AvlTreeNode<T> right) {
    if (right == null) {
      this.right = null;
      return;
    }

    int comp = right.getValue().compareTo(this.getValue());

    if (comp > 0) {
      this.right = right;
      return;
    } else {
      throw new RuntimeException("Right child must be greater than parent");
    }
  }

  public boolean isLeaf() {
    return left == null && right == null;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public List<TreeNode<T>> getChildren() {
    List<TreeNode<T>> children = new ArrayList<>();
    if (left != null) {
      children.add(left);
    }
    if (right != null) {
      children.add(right);
    }
    return children;
  }
}

// Traversers -----------------------------------------------------------------

@SuppressWarnings("unchecked")
class LevelOrderTraverser<T extends Comparable<? super T>, N extends TreeNode<T>>
    implements Iterator<N> {
  private Queue<N> q = new LinkedList<N>();

  public LevelOrderTraverser(N root) {
    q = new LinkedList<>();
    if (root != null) {
      q.add(root);
    }
  }

  @Override
  public boolean hasNext() {
    return !q.isEmpty();
  }

  @Override
  public N next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more elements in the tree");
    }

    N current = q.poll();

    for (N child : (List<? extends N>) current.getChildren()) {
      q.add(child);
    }
    return current;
  }
}

@SuppressWarnings("unchecked")
class PostOrderTraverser<T extends Comparable<? super T>, N extends BinarySearchTreeNode<T>>
    implements Iterator<N> {
  private final Stack<N> stack;
  private final Stack<N> output;

  public PostOrderTraverser(N root) {
    stack = new Stack<>();
    output = new Stack<>();
    if (root != null) {
      stack.push(root);
    }
    while (!stack.isEmpty()) {
      N current = stack.pop();
      output.push(current);
      if (current.getLeftChild() != null) {
        stack.push((N) current.getLeftChild());
      }
      if (current.getRightChild() != null) {
        stack.push((N) current.getRightChild());
      }
    }
  }

  @Override
  public boolean hasNext() {
    return !output.isEmpty();
  }

  @Override
  public N next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more elements in the tree");
    }
    return output.pop();
  }
}

@SuppressWarnings("unchecked")
class PreOrderTraverser<T extends Comparable<? super T>, N extends BinarySearchTreeNode<T>>
    implements Iterator<N> {
  private Stack<N> stack;

  public PreOrderTraverser(N root) {
    stack = new Stack<>();
    if (root != null) {
      stack.push(root);
    }
  }

  @Override
  public boolean hasNext() {
    return !stack.isEmpty();
  }

  @Override
  public N next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    N node = stack.pop();
    if (node.getRightChild() != null) {
      stack.push((N) node.getRightChild());
    }
    if (node.getLeftChild() != null) {
      stack.push((N) node.getLeftChild());
    }
    return node;
  }
}

// Dot files ------------------------------------------------------------------

class DotFileReader {

  public static <T extends Comparable<? super T>> GenericTree<T> createTreeFromDotFile(
      String filePath, Function<String, T> converter) throws IOException {
    GenericTree<T> tree = new GenericTree<T>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isTreeContent = false;

      while ((line = reader.readLine()) != null) {
        line = line.trim();

        // Skip "digraph {" or "graph {" and "}" lines
        if (line.startsWith("digraph") || line.startsWith("graph")) {
          isTreeContent = true;
          continue;
        }
        if (line.equals("}")) {
          isTreeContent = false;
          break;
        }

        if (isTreeContent && line.contains("->")) {
          String[] parts = line.split("->");
          if (parts.length == 2) {
            T parentValue = converter.apply(parts[0].trim());
            T childValue = converter.apply(parts[1].replace(";", "").trim());

            GenericTreeNode<T> parent = tree.searchNode(parentValue);
            GenericTreeNode<T> child = new GenericTreeNode<>(childValue);

            if (parent == null) {
              GenericTreeNode<T> rootNode = new GenericTreeNode<T>(parentValue);
              tree = new GenericTree<>(rootNode);
              rootNode.addChild(child);
            } else {
              parent.addChild(child);
            }
          }
        }
      }
    }
    return tree;
  }
}

class DotFileWriter {
  public static void toDotFile(String outputFileName, DirectoryTraverser directoryTree) {
    try (FileWriter writer = new FileWriter(outputFileName)) {
      writer.write("digraph FolderStructure {\n");
      writer.write("  node [shape=box];\n");
      writeNodeConnections(directoryTree.getDirectoryTree(), writer);
      writer.write("}\n");
      System.out.println("Graph written to " + outputFileName);
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }
  }

  private static void writeNodeConnections(GenericTree<File> tree, FileWriter writer)
      throws IOException {
    String rootValue = tree.getRoot().getValue().getName();
    writer.write(String.format("  \"%s\" [label=\"%s\"];\n", rootValue, rootValue));
    for (GenericTreeNode<File> node : tree.levelOrderTraverse()) {
      for (GenericTreeNode<File> child : node.getChildren()) {
        // Draw an edge from the current node to each of its children
        writer.write(
            String.format(
                "  \"%s\" -> \"%s\";\n", node.getValue().getName(), child.getValue().getName()));
      }
    }
  }
}

class DirectoryTraverser {

  private String rootPath;
  private GenericTree<File> directoryTree;

  public DirectoryTraverser(String rootPath) {
    this.rootPath = rootPath;
    createTree();
  }

  private void createTree() {
    File root = new File(rootPath);

    if (!root.exists()) {
      throw new IllegalArgumentException("Path does not exist: " + root.getAbsolutePath());
    }

    GenericTreeNode<File> rootNode = new GenericTreeNode<File>(root);
    directoryTree = new GenericTree<>(rootNode);
    Queue<GenericTreeNode<File>> queue = new LinkedList<GenericTreeNode<File>>();
    queue.add(rootNode);

    while (!queue.isEmpty()) {
      GenericTreeNode<File> current = queue.remove();
      if (current.getValue().isDirectory()) {
        File[] files = current.getValue().listFiles();
        if (files != null) {
          for (File file : files) {
            GenericTreeNode<File> nextChild = new GenericTreeNode<File>(file);
            directoryTree.addChild(current, nextChild);
            queue.add(nextChild);
          }
        }
      }
    }
  }

  public GenericTree<File> getDirectoryTree() {
    return directoryTree;
  }

  public Iterable<GenericTreeNode<File>> traverse() {
    return directoryTree.levelOrderTraverse();
  }

  public void writeTraversalToDotFile(String outputFileName) {
    DotFileWriter.toDotFile(outputFileName, this);
  }
}
