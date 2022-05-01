package civCombat;

class Pair<T, T1> {
  T left;
  T1 right;

  Pair(T left, T1 right) {
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
