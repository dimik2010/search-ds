package ru.mail.polis;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class AVLTree<E extends Comparable<E>> extends AbstractSet<E> implements BalancedSortedSet<E> {


  private final Comparator<E> comparator;

  class Node {
    E value;
    Node left;
    Node right;
    int height = 1;

    public Node(E value) {
      this.value = value;
    }

  }

  private Node root;
  private int size;

  public AVLTree() {
    this(null);
  }

  public AVLTree(Comparator<E> comparator) {
    this.comparator = comparator;
  }

  /**
   * Вставляет элемент в дерево.
   * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
   *
   * @param value элемент который необходимо вставить
   * @return true, если элемент в дереве отсутствовал
   */
  @Override
  public boolean add(E value) {
    int oldSize = size;
    root = insert(value, root);
    if (oldSize == size) {
      return false;
    }
    return true;
  }

  private Node insert(E value, Node root) {
    if (root == null) {
      size++;
      Node newNode = new Node(value);
      return newNode;
    }
    if (compare(value, root.value) == 0) {
      return root;
    }
    if (compare(value, root.value) < 0) {
      root.left = insert(value, root.left);
    } else
      root.right = insert(value, root.right);
    return balanceNode(root);
  }

  /**
   * Удаляет элемент с таким же значением из дерева.
   * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
   *
   * @param object элемент который необходимо вставить
   * @return true, если элемент содержался в дереве
   */
  @Override
  public boolean remove(Object object) {
    @SuppressWarnings("unchecked")
    E value = (E) object;
    int oldSize = size;
    root = remove(root, value);
    if (oldSize == size)
      return false;
    return true; //TODO return false
  }

  private Node remove(Node node, E value) {
    if (node == null) {
      return null;
    }
    if (compare(value, node.value) == 0) {
      if (node.right == null)
        return node.left;
      Node minNode = getMinNode(node.right);
      minNode.right = removeMinNode(node.right);
      minNode.left = node.left;
      size--;
      return balanceNode(minNode);
    }
    if (compare(value, node.value) > 0) {
      node.right = remove(node.right, value);
    } else {
      node.left = remove(node.left, value);
    }
    return balanceNode(node);
  }

  private Node removeMinNode(Node node) {
    if (node.left == null)
      return node.right;
    node.left = removeMinNode(node);
    return balanceNode(node);
  }

  private Node getMinNode(Node node) {
    while (node.left != null) {
      node = node.left;
    }
    return node;
  }

  /**
   * Ищет элемент с таким же значением в дереве.
   * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
   *
   * @param object элемент который необходимо поискать
   * @return true, если такой элемент содержится в дереве
   */
  @Override
  public boolean contains(Object object) {
    @SuppressWarnings("unchecked")
    E value = (E) object;
    Node curr = root;
    while (curr != null) {
      if (compare(value, curr.value) == 0)
        return true;
      if (compare(value, curr.value) > 0) {
        curr = curr.right;
      } else {
        curr = curr.left;
      }
    }
    return false;
  }

  /**
   * Ищет наименьший элемент в дереве
   *
   * @return Возвращает наименьший элемент в дереве
   * @throws NoSuchElementException если дерево пустое
   */
  @Override
  public E first() {
    if (root == null) {
      throw new NoSuchElementException("first");
    }
    return getMinNode(root).value;
  }

  /**
   * Ищет наибольший элемент в дереве
   *
   * @return Возвращает наибольший элемент в дереве
   * @throws NoSuchElementException если дерево пустое
   */
  @Override
  public E last() {
    if (root == null) {
      throw new NoSuchElementException("last");
    }
    Node curr = root;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr.value;
  }

  private int compare(E v1, E v2) {
    return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
  }

  @Override
  public Comparator<? super E> comparator() {
    return comparator;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("AVLTree{" +
            "tree=" + root +
            "size=" + size +
            ", elements: "
    );
    traverseTree(root, stringBuilder);
    return stringBuilder.toString();
  }

  private void traverseTree(Node node, StringBuilder stringBuilder) {
    if (node == null)
      return;
    traverseTree(node.left, stringBuilder);
    stringBuilder.append(node.value);
    stringBuilder.append(" ");
    traverseTree(node.right, stringBuilder);
  }

  @Override
  public SortedSet<E> subSet(E fromElement, E toElement) {
    throw new UnsupportedOperationException("subSet");
  }

  @Override
  public SortedSet<E> headSet(E toElement) {
    throw new UnsupportedOperationException("headSet");
  }

  @Override
  public SortedSet<E> tailSet(E fromElement) {
    throw new UnsupportedOperationException("tailSet");
  }

  @Override
  public Iterator<E> iterator() {
    throw new UnsupportedOperationException("iterator");
  }

  /**
   * Обходит дерево и проверяет что высоты двух поддеревьев
   * различны по высоте не более чем на 1
   *
   * @throws NotBalancedTreeException если высоты отличаются более чем на один
   */
  @Override
  public void checkBalanced() throws NotBalancedTreeException {
    traverseTreeAndCheckBalanced(root);
  }

  private Node rotateLeft(Node node) {
    Node newRoot = node.right;
    node.right = newRoot.left;
    newRoot.left = node;
    fixHeight(node);
    fixHeight(newRoot);
    return newRoot;
  }

  private Node rotateRight(Node node) {
    Node newRoot = node.left;
    node.left = newRoot.right;
    newRoot.right = node;
    fixHeight(node);
    fixHeight(newRoot);
    return newRoot;
  }

  private Node makeBigLeftRotate(Node node) {
    node.right = rotateRight(node.right);
    return rotateLeft(node);
  }

  private Node makeBigRightRotate(Node node) {
    node.left = rotateLeft(node.left);
    return rotateRight(node);
  }

  private Node balanceNode(Node node) {
    fixHeight(node);
    if (calcDiff(node) == 2) {
      if (calcDiff(node.right) < 0) {
        return makeBigLeftRotate(node);
      } else {
        return rotateLeft(node);
      }
    } else if (calcDiff(node) == -2) {
      if (calcDiff(node.left) > 0) {
        return makeBigRightRotate(node);
      } else {
        return rotateRight(node);
      }
    }
    return node;
  }

  private int getHeight(Node node) {
    return node == null ? 0 : node.height;
  }


  private void fixHeight(Node node) {
    node.height = Math.max(getHeight(node.right), getHeight(node.left)) + 1;
  }

  private int calcDiff(Node node) {
    return (node.right != null ? node.right.height : 0) - (node.left != null ? node.left.height : 0);
  }


  private int traverseTreeAndCheckBalanced(Node curr) throws NotBalancedTreeException {
    if (curr == null) {
      return 1;
    }
    int leftHeight = traverseTreeAndCheckBalanced(curr.left);
    int rightHeight = traverseTreeAndCheckBalanced(curr.right);
    if (Math.abs(leftHeight - rightHeight) > 1) {
      throw NotBalancedTreeException.create("The heights of the two child subtrees of any node must be differ by at most one",
              leftHeight, rightHeight, curr.toString());
    }
    return Math.max(leftHeight, rightHeight) + 1;
  }

  public static void main(String[] args) {
    AVLTree<Integer> avlTree = new AVLTree<>();
    System.out.println(avlTree.add(5));
    System.out.println(avlTree.add(5));
    System.out.println(avlTree);
    for (int i = 0; i < 10; i++) {
      System.out.println(avlTree.add(5));
      System.out.println("" + i + ": " +avlTree.add(i) + " " + avlTree.contains(i));
    }
    System.out.println(avlTree);
  }

}
