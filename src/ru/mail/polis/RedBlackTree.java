package ru.mail.polis;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;


public class RedBlackTree<E extends Comparable<E>> extends AbstractSet<E> implements BalancedSortedSet<E> {

  private final Comparator<E> comparator;
  private Node root;
  private int size;

  public RedBlackTree() {
    this(null);
  }

  public RedBlackTree(Comparator<E> comparator) {
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
    Node curr = root;
    Node parent = null;
    if (root == null) {
      root = new Node(value);
      root.color = Color.BLACK;
      size++;
      return true;
    }
    while (curr != null) {
      parent = curr;
      if (compare(value, curr.value) == 0)
        return false;
      if (compare(value, curr.value) > 0) {
        curr = curr.right;
      } else {
        curr = curr.left;
      }
    }
    if (compare(value, parent.value) > 0) {
      parent.right = new Node(value);
      curr = parent.right;
      curr.parent = parent;
    } else {
      parent.left = new Node(value);
      curr = parent.left;
      curr.parent = parent;
    }
    if (parent.color != Color.BLACK) {
      fixColors(curr);
    }
    size++;
    return true;
  }

  private void fixColors(Node curr) {
    Node uncle;
    while (curr.parent != null && curr.parent.color == Color.RED) {
      if (curr.parent == curr.parent.parent.left) { //if curr.parent - left grandfather's child
        uncle = curr.parent.parent.right; // grandfather is not null, coz it's not root and parent is red
        if (uncle == null || uncle.color == Color.BLACK) { // uncle is right grandfather's child
          if (curr == curr.parent.right) {
            rotateLeft(curr.parent); // for making big right rotate if curr is right son
            curr = curr.left;
          }
          curr.parent.color = Color.BLACK; //change colors of father
          curr.parent.parent.color = Color.RED; // and grandfather
          rotateRight(curr.parent.parent);
        } else { //uncle.color == RED
          curr.parent.color = Color.BLACK; //change colors of father
          uncle.color = Color.BLACK; // and uncle
          if (curr.parent.parent != root) {
            curr.parent.parent.color = Color.RED; //and granddad if he is not root
          }
          curr = curr.parent.parent; //for checking colors rules of grandparent
        }

      } else { // curr.parent - right grandfather's child , same as left just change left->right
        uncle = curr.parent.parent.left; // grandfather is not null, coz it's not root and parent is red
        if (uncle == null || uncle.color == Color.BLACK) { // uncle is right grandfather's child
          if (curr == curr.parent.left) {
            rotateRight(curr.parent); // for making big left rotate
            curr = curr.right;
          }
          curr.parent.color = Color.BLACK;
          curr.parent.parent.color = Color.RED;
          rotateLeft(curr.parent.parent);
        } else { //uncle.color == RED
          curr.parent.color = Color.BLACK;
          uncle.color = Color.BLACK;
          if (curr.parent.parent != root) {
            curr.parent.parent.color = Color.RED;
          }
          curr = curr.parent.parent;
        }
      }
    }
  }

  private void rotateLeft(Node node) {
    Node newRoot = node.right;
    if (node == root) {
      root = newRoot;
    } else if (node.parent.right == node) {
      node.parent.right = newRoot;
    } else {
      node.parent.left = newRoot;
    }
    newRoot.parent = node.parent;
    node.parent = newRoot;
    if (newRoot.left != null) {
      newRoot.left.parent = node;
    }
    node.right = newRoot.left;
    newRoot.left = node;
  }

  private void rotateRight(Node node) {
    Node newRoot = node.left;
    if (node.parent == null) {
      root = newRoot;
    } else if (node.parent.right == node) {
      node.parent.right = newRoot;
    } else {
      node.parent.left = newRoot;
    }
    newRoot.parent = node.parent;
    node.parent = newRoot;
    if (newRoot.right != null) {
      newRoot.right.parent = node;
    }
    node.left = newRoot.right;
    newRoot.right = node;
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
    //todo: следует реализовать
    return false;
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
    if (size == 0) {
      throw new NoSuchElementException("first");
    }
    Node curr = root;
    while (curr.left != null) {
      curr = curr.left;
    }
    return curr.value;
  }

  /**
   * Ищет наибольший элемент в дереве
   *
   * @return Возвращает наибольший элемент в дереве
   * @throws NoSuchElementException если дерево пустое
   */
  @Override
  public E last() {
    if (size == 0) {
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
    return "RBTree{" +
            "size=" + size + ", " +
            "tree=" + root +
            '}';
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
   * Обходит дерево и проверяет выполнение свойств сбалансированного красно-чёрного дерева
   * <p>
   * 1) Корень всегда чёрный.
   * 2) Если узел красный, то его потомки должны быть чёрными (обратное не всегда верно)
   * 3) Все пути от узла до листьев содержат одинаковое количество чёрных узлов (чёрная высота)
   *
   * @throws NotBalancedTreeException если какое-либо свойство невыполнено
   */
  @Override
  public void checkBalanced() throws NotBalancedTreeException {
    if (root != null) {
      if (root.color != Color.BLACK) {
        throw new NotBalancedTreeException("Root must be black");
      }
      traverseTreeAndCheckBalanced(root);
    }
  }

  private int traverseTreeAndCheckBalanced(Node node) throws NotBalancedTreeException {
    if (node == null) {
      return 1;
    }
    int leftBlackHeight = traverseTreeAndCheckBalanced(node.left);
    int rightBlackHeight = traverseTreeAndCheckBalanced(node.right);
    if (leftBlackHeight != rightBlackHeight) {
      throw NotBalancedTreeException.create("Black height must be equal.", leftBlackHeight, rightBlackHeight, node.toString());
    }
    if (node.color == Color.RED) {
      checkRedNodeRule(node);
      return leftBlackHeight;
    }
    return leftBlackHeight + 1;
  }

  private void checkRedNodeRule(Node node) throws NotBalancedTreeException {
    if (node.left != null && node.left.color != Color.BLACK) {
      throw new NotBalancedTreeException("If a node is red, then left child must be black.\n" + node.toString());
    }
    if (node.right != null && node.right.color != Color.BLACK) {
      throw new NotBalancedTreeException("If a node is red, then right child must be black.\n" + node.toString());
    }
  }

  enum Color {
    RED, BLACK
  }

  class Node {
    E value;
    Node left;
    Node right;
    Node parent;
    Color color = Color.RED;

    public Node(E value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return "Node{" +
              "value=" + value +
              ", left=" + left +
              ", right=" + right +
              ", color=" + color +
              "}";
    }
  }

  public static void main(String[] args) throws NotBalancedTreeException {
    RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
    System.out.println(redBlackTree.add(2));
    System.out.println(redBlackTree.add(3));
    System.out.println(redBlackTree.add(3));
//    redBlackTree.checkBalanced();
    for (int i = 0; i < 10; i++) {
      System.out.println("i " + i + " " + redBlackTree.add(i));
    }
    System.out.println(redBlackTree.root);
    redBlackTree.checkBalanced();

  }
}
