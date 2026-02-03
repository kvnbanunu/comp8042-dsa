/*
 * BCIT COMP 8042
 * Lab Assignment 2
 * Stack Section
 * Kevin Nguyen
 * A00955925
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
  private ArrayList<T> list;
  private int length;

  public ArrayListStack() {
    list = new ArrayList<T>();
    length = 0;
  }

  public ArrayListStack(int n) {
    list = new ArrayList<>(n);
    length = n;
  }

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

  record Parenthesis(char p, int line) {}

  public static void validParentheses(String fileName) {
    File file = new File(fileName);
    boolean res = true;
    ArrayListStack<Parenthesis> stack = new ArrayListStack<>();
    int currentLine = 0;

    try (Scanner scan = new Scanner(file)) {
      while (scan.hasNextLine() && res) {
        String line = scan.nextLine();
        currentLine++;
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          if (c == '(' || c == '{' || c == '[') {
            stack.push(new Parenthesis(c, currentLine));
          } else if (c == ')' || c == '}' || c == ']') {
            Parenthesis check = stack.pop();
            if (check == null
                || (c == ')' && check.p != '(')
                || (c == '}' && check.p != '{')
                || (c == ']' && check.p != '[')) {
              res = false;
              P("Invalid closing parenthesis: " + c + " at line " + currentLine);
              break;
            }
          }
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }

    if (!stack.empty() && res) {
      res = false;
      while (!stack.empty()) {
        Parenthesis curr = stack.pop();
        P(
            "Opening parenthesis: "
                + curr.p()
                + " at line "
                + curr.line()
                + " missing closing parenthesis");
      }
    }

    if (res) {
      P("Valid parentheses");
    }
  }

  public static void main(String[] args) {
    P("Task1");
    validParentheses("StackParenthesesTask1.java");
    P("\nTask2");
    validParentheses("StackParenthesesTask2.java");
    P("\nTask3");
    validParentheses("StackParenthesesTask3.java");
    P("\nTask4");
    validParentheses("StackParenthesesTask4.java");
  }

  public static void P(String s) {
    System.out.println(s);
  }
}
