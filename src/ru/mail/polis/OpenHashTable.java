package ru.mail.polis;

import java.util.*;

public class OpenHashTable<E extends OpenHashTableEntity> extends AbstractSet<E> implements Set<E> {
  private static final int INITIAL_CAPACITY = 16;
  private int capacity = INITIAL_CAPACITY;
  private int size = 0;
  private E[] table;
  private boolean[] deleted = new boolean[INITIAL_CAPACITY];

  @SuppressWarnings("unchecked")
  public OpenHashTable() {
    table = (E[]) new OpenHashTableEntity[INITIAL_CAPACITY];
  }

  /**
   * Вставляет элемент в хеш-таблицу.
   * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
   *
   * @param value элемент который необходимо вставить
   * @return true, если элемент в хеш-таблице отсутствовал
   */
  @Override
  public boolean add(E value) {
    //Используйте value.hashCode(capacity, probId) для вычисления хеша
    for (int i = 0; i < capacity; i++) {
      int hash = value.hashCode(capacity, i);
      if (table[hash] == null || deleted[hash]) {
        table[value.hashCode(capacity, i)] = value;
        deleted[hash] = false;
        size++;
        if (size > capacity/2) {
          increaseCapacity();
        }
        return true;
      }
      if (table[hash].equals(value))
        return false;
    }
    return false;
  }

  /**
   * Rehashing and increasing capacity
   */
  @SuppressWarnings("unchecked")
  private void increaseCapacity() {
    E[] tempTable = Arrays.copyOf(table, table.length); //DO BETTER
    boolean[] oldDeleted = Arrays.copyOf(deleted, deleted.length);
    table = (E[]) new OpenHashTableEntity[capacity*=2];
    deleted = new boolean[capacity];
    size = 0;
    for (int i = 0; i < tempTable.length; i++) {
      if (tempTable[i] != null && !oldDeleted[i]) {
        add(tempTable[i]);
      }
    }
  }

  /**
   * Удаляет элемент с таким же значением из хеш-таблицы.
   * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
   *
   * @param object элемент который необходимо вставить
   * @return true, если элемент содержался в хеш-таблице
   */
  @Override
  public boolean remove(Object object) {
    @SuppressWarnings("unchecked")
    E value = (E) object;
    for (int i = 0; i < capacity; i++) {
      int hash = value.hashCode(capacity, i);
      if (table[hash] != null ) {
        if (table[hash].equals(value) && !deleted[hash]) {
          deleted[hash] = true;
          size--;
          return true;
        }
      } else {
        return false;
      }
    }
    return false;
  }

  /**
   * Ищет элемент с таким же значением в хеш-таблице.
   * Инвариант: на вход всегда приходит NotNull объект, который имеет корректный тип
   *
   * @param object элемент который необходимо поискать
   * @return true, если такой элемент содержится в хеш-таблице
   */
  @Override
  public boolean contains(Object object) {
    @SuppressWarnings("unchecked")
    E value = (E) object;
    for (int i = 0; i < capacity; i++) {
      int hash = value.hashCode(capacity, i);
      if (table[hash] != null) {
        if (table[hash].equals(value) && !deleted[hash]) {
          return true;
        }
      } else {
        return false;
      }
    }
    return false;
  }

  public int getCapacity() {
    return capacity;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<E> iterator() {
    throw new UnsupportedOperationException();
  }

}
