package trees;

import java.util.*;
import trees.TreeNodes.GenericTreeNode;
import trees.TreeTraversers.*;

public class GenericTree<T extends Comparable<? super T>> {
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
