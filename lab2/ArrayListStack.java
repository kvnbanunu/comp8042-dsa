/*
 * BCIT COMP 8042
 * Lab Assignment 2
 * Stack Section
 * Kevin Nguyen
 * A00955925
 */
import java.util.ArrayList;

interface Stack<T> {
  /** Tests if this stack is empty. */
  boolean empty();

  /** Looks at the object at the top of this stack without removing it from the stack. */
  T peek();

  /**
   * Removes the object at the top of this stack and returns that object as the value of this
   * function.
   */
  T pop();

  /** Pushes an item onto the top of this stack. */
  void push(T item);
}

public class ArrayListStack<T> implements Stack<T> {
  private ArrayList<T> list = new ArrayList<T>();
  private int length = 0;

  @Override
  public boolean empty() {
    return length == 0;
  }

  @Override
  public T peek() {
    if (empty()) {
      return null;
    }
    return list.get(length - 1);
  }

  @Override
  public T pop() {
    T res = peek();
    if (length > 0) {
      list.remove(length - 1);
      length--;
    }
    return res;
  }

  @Override
  public void push(T item) {
    list.add(item);
    length++;
  }

  public static void main(String[] args) {}
}
