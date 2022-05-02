package CivCombat;

/**
 * Contains two objects.
 */
public class Pair<T, T1> {
  T left;
  T1 right;

  public Pair(T left, T1 right) {
    this.left = left;
    this.right = right;
  }

  public T getLeft() {
    return left;
  }

  public T1 getRight() {
    return right;
  }
}
