/*
 * BCIT COMP 8042
 * Lab Assignment 3
 * Binary Search Tree Check
 * Kevin Nguyen
 * A00955925
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

public class BSTChecker {
  boolean isBinarySearchTree(GenericTree<Integer> tree) {
    return false;
  }
}

/*****************************************************************************
 *                                                                           *
 *         All code found below was provided to complete this lab            *
 *                                                                           *
 *****************************************************************************/

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

// ------------------------------Nodes-----------------------------------------

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

interface BinaryTreeNode<T> {
  public BinaryTreeNode<T> getRightChild();

  public BinaryTreeNode<T> getLeftChild();

  public void setRightChild(T right);

  public void setLeftChild(T left);

  public int getHeight();

  public void setHeight(int height);
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

// ------------------------------Traversal-------------------------------------

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
