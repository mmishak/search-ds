package ru.mail.polis;

import java.util.Comparator;

//TODO: write code here
public class OpenHashTable<E extends Comparable<E>> implements ISet<E> {

    class Deleted extends Object {
        @Override
        public int hashCode() {
            return -1;
        }

        @Override
        public String toString() {
            return "Deleted";
        }
    }

    private Comparator<E> comparator;

    private final int INITIAL_CAPACITY = 8;
    private Object[] table;
    private int size;

    public OpenHashTable() {
        this(null);
    }

    public OpenHashTable(Comparator<E> comparator) {
        this.comparator = comparator;
        this.table = new Object[INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E value) {
        int h1 = hash(value, 0);
        int h2 = hash(value, 1);
        for (int i = 0; i < table.length; i++) {
            if (table[h1] != null) {
                if (h1 != -1 && table[h1].equals(value))
                    return true;
            } else
                return false;
            h1 = (h1 + h2) % table.length;
        }
        return false;
    }

    @Override
    public boolean add(E value) {

        resize();

        int h1 = hash(value, 0);
        int h2 = hash(value, 1);
        for (int i = 0; i < table.length; i++) {
            if (table[h1] == null || h1 == -1) {
                table[h1] = value;
                size++;
                return true;
            } else if (table[h1].equals(value))
                return false;
            h1 = (h1 + h2) % table.length;
        }

        return false;
    }

    @Override
    public boolean remove(E value) {
        int h1 = hash(value, 0);
        int h2 = hash(value, 1);
        for (int i = 0; i < table.length; i++) {
            if (table[h1] != null) {
                if (h1 != -1 && table[h1].equals(value)) {
                    table[h1] = new Deleted();
                    size--;
                    return true;
                }
            } else {
                return false;
            }
            h1 = (h1 + h2) % table.length;
        }
        return false;
    }

    private int hash(Object value, int type) {
        if (value.toString().equals("Deleted"))
            return -1;

        int hash;
        switch (type) {
            case 0: {
                hash = 7;
                for (int i = 0; i < ((String) value).length(); i++) {
                    hash = hash * 31 + ((String) value).charAt(i);
                }
                hash = Math.abs(hash) % table.length;
                break;
            }
            case 1: {
                hash = ((String) value).length() % (table.length - 1);
                if (hash % 2 == 0)
                    hash++;
                break;
            }
            default:
                return -2;
        }

        return hash;
    }

    private void print() {
        for (int i = 0; i < table.length; i++) {
            System.out.println("idx = " + i + ", " + table[i].toString());
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if (size * 2 < table.length) {
            return;
        }
        Object[] old = this.table;
        size = 0;
        table = new Object[table.length << 1];
        for (int i = 0; i < old.length; i++) {
            if (old[i] == null || hash(old[i], 0) == -1)
                continue;
            add((E) old[i]);
            old[i] = null;
        }
    }
}
