package ru.mail.polis;

import java.util.SortedSet;

public interface BalancedSortedSet<E> extends SortedSet<E> {

  /**
   * Обходит дерево и проверяет сбалансированность
   *
   * @throws NotBalancedTreeException если дерево не сбалансированное с указанием дефектного узла
   */
  void checkBalanced() throws NotBalancedTreeException;
}
