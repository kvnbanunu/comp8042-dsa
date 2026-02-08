package trees;

public class BinarySearchTreeRunner {

  public static void main(String[] args) {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    int[] values = {10, 5, 15, 3, 7, 12, 18};

    for (Integer value : values) {
      tree.insert(value);
    }

    tree.printTree();
  }
}
