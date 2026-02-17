/*
 * BCIT COMP 8042
 * Lab Assignment 3
 * AVL Tree Contact List
 * Kevin Nguyen
 * A00955925
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Function;

public class ContactList {
  private AvlTree<Contact> contacts;
  private ArrayList<Contact> inOrder;
  private int size;
  private boolean edited;

  public ContactList() {
    contacts = new AvlTree<Contact>();
    size = 0;
    edited = false;
  }

  public void insertContact(Contact c) {
    if (findContact(c.getName()) != null) {
      return;
    }
    contacts.insert(c);
    size++;
    edited = true;
  }

  public Contact findContact(String name) {
    // can be null
    return findContact(name, contacts.getRoot());
  }

  // recursive helper for findContact
  private Contact findContact(String name, AvlTreeNode<Contact> root) {
    if (root == null) return null;

    int comparison = name.compareToIgnoreCase(root.getValue().getName());

    if (comparison == 0) {
      return root.getValue();
    } else if (comparison < 0) {
      return findContact(name, root.getLeftChild());
    } else {
      return findContact(name, root.getRightChild());
    }
  }

  public void removeContact(String name) {
    Contact found = findContact(name);
    if (found == null) return;

    contacts.remove(found);
    size--;
    edited = true;

    System.out.println("Removed - " + found);
  }

  // inOrderTraversal for AVLTree to help with the getList methods
  private void inOrderTraverse(AvlTreeNode<Contact> node) {
    if (node == null) return;
    inOrderTraverse(node.getLeftChild());
    inOrder.add(node.getValue());
    inOrderTraverse(node.getRightChild());
  }

  public List<Contact> getEveryContact() {
    if (size == 0) {
      return new ArrayList<>();
    }
    if (inOrder != null && !edited) {
      return inOrder;
    }
    inOrder = new ArrayList<>(size);
    inOrderTraverse(contacts.getRoot());
    edited = false;
    return inOrder;
  }

  public List<Contact> getEveryContactStartingWith(char letter) {
    if (size == 0) {
      return new ArrayList<>();
    }
    if (inOrder == null || edited) {
      inOrder = new ArrayList<>(size);
      inOrderTraverse(contacts.getRoot());
      edited = false;
    }

    int[] bounds = findBounds(letter);
    if (bounds[0] == 0) {
      return new ArrayList<>(); // empty
    }

    return inOrder.subList(bounds[1], bounds[2]);
  }

  private int[] findBounds(char letter) {
    int l = 0;
    int r = size - 1;

    while (l <= r) {
      int m = l + (r - l) / 2;
      if (inOrder.get(m).getName().toLowerCase().charAt(0) == letter) {
        l = m;
        r = m;

        while (inOrder.get(l).getName().toLowerCase().charAt(0) == letter) {
          l--;
        }

        while (inOrder.get(r).getName().toLowerCase().charAt(0) == letter) {
          r++;
        }

        return new int[] {1, l + 1, r};

      } else if (inOrder.get(m).getName().toLowerCase().charAt(0) > letter) {
        r = m - 1;
      } else {
        l = m + 1;
      }
    }

    return new int[] {0};
  }

  public List<Contact> getStringMatchingContacts(String segment) {
    if (size == 0) {
      return new ArrayList<>();
    }
    if (inOrder == null || edited) {
      inOrder = new ArrayList<>(size);
      inOrderTraverse(contacts.getRoot());
      edited = false;
    }

    List<Contact> list = new ArrayList<>(size);

    for (Contact c : inOrder) {
      if (contains(c.getName(), segment)) {
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

  // generates a random string of 10 numbers
  private static String randPhone() {
    String res = "";
    for (int i = 0; i < 11; i++) {
      res += (int) (Math.random() * 10);
    }
    return res;
  }

  public static void main(String[] args) {
    ContactList cl = new ContactList();
    cl.insertContact(new Contact("Yang, Victor", "vyang@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Laviolette, Lucas", "llaviolette@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Chuah, Josh", "jchuah@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Reziapov, Timur", "treziapov@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Li, Louise", "lli@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Lin, Joe", "jlin@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Seo, Brian", "bseo@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Zohoori Rad, Sepehr", "szohoorirad@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Sue, Wilson", "wsue@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Dunnet, Kohei", "@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Gonzales, Evin", "egonzales@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Nguyen, Kevin", "knguyen@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Bartyuk, Nathan", "nbartyuk@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Ramdev, Madhav", "mrandev@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Surilla, Raziel", "rsurilla@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Guo, John", "jguo@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Hunter, Clay", "chunter@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Clarito, Fonse", "fclarito@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Winaldo, Nicholas", "nwinaldo@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Melnick, Reese", "rmelnick@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Purtell, Keagan", "kpurtell@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Baek, Seung Jae", "sjbaek@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Rada, Brandon", "brada@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Klein, Blaise", "bklein@bcit.ca", randPhone()));
    cl.insertContact(new Contact("Angelozzi, Lucas", "langelozzi@bcit.ca", randPhone()));

    System.out.println("Finding Contact with name 'Nguyen, Kevin'");

    Contact found = cl.findContact("Nguyen, Kevin");
    if (found != null) {
      System.out.println(found);
    } else {
      System.out.println("Contact: Nguyen, Kevin not found.");
    }

    System.out.println("\nEvery Contact in alphabetical order");
    for (Contact c : cl.getEveryContact()) {
      System.out.println(c);
    }

    System.out.println("\nEvery Contact starting with 'l'");
    for (Contact c : cl.getEveryContactStartingWith('l')) {
      System.out.println(c);
    }

    System.out.println("\nEvery Contact containing the string: 'la'");
    for (Contact c : cl.getStringMatchingContacts("la")) {
      System.out.println(c);
    }

    System.out.println(
        "\n"
            + "Removing Contacts: [Nguyen, Kevin; Laviolette, Lucas; Clarito Fonse; Winaldo,"
            + " Nicholas]");
    cl.removeContact("Nguyen, Kevin");
    cl.removeContact("Laviolette, Lucas");
    cl.removeContact("Clarito, Fonse");
    cl.removeContact("Winaldo, Nicholas");

    System.out.println("\nRerunning tests...");

    System.out.println("\nFinding Contact with name 'Nguyen, Kevin'");

    found = cl.findContact("Nguyen, Kevin");
    if (found != null) {
      System.out.println(found);
    } else {
      System.out.println("Contact: Nguyen, Kevin not found.");
    }
    System.out.println("\nEvery Contact in alphabetical order");
    for (Contact c : cl.getEveryContact()) {
      System.out.println(c);
    }

    System.out.println("\nEvery Contact starting with 'l'");
    for (Contact c : cl.getEveryContactStartingWith('l')) {
      System.out.println(c);
    }

    System.out.println("\nEvery Contact containing the string: 'la'");
    for (Contact c : cl.getStringMatchingContacts("la")) {
      System.out.println(c);
    }
  }
}

// needs to implement Comparable to work as an AvlTreeNode
class Contact implements Comparable<Contact> {
  private String name;
  private String email;
  private String phone;

  @Override
  public int compareTo(Contact other) {
    return this.name.compareToIgnoreCase(other.name);
  }

  public Contact(String name, String email, String phone) {
    this.name = name;
    this.email = email;
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String toString() {
    return String.format("Name: %-20s Email: %-20s Phone: %s", name, email, phone);
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
