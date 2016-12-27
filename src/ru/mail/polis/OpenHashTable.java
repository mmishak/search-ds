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
        int idx = hash(value);

        int count = 0;
        while (count < size && table[idx] != null) {

            if (table[idx].equals(value)) {
                return true;
            }

            if (table[idx] == null)
                break;

            count++;
            idx++;
            if (idx == table.length)
                idx = 0;
        }

        return false;
    }

    @Override
    public boolean add(E value) {
        resize();
        int idx = hash(value);
        if (table[idx] == null || hash(table[idx]) == -1) {
            table[idx] = value;
            size++;
            return true;
        } else {
            int count = 0;
            while (count < table.length) {

                if (table[idx] == value)
                    return false;

                if (table[idx] == null || hash(table[idx]) == -1) {
                    table[idx] = value;
                    size++;
                    return true;
                }

                count++;
                idx++;
                if (idx == table.length)
                    idx = 0;
            }
        }
        return false;
    }

    @Override
    public boolean remove(E value) {
        int idx = hash(value);

        int count = 0;
        while (count < size && table[idx] != null) {

            if (table[idx].equals(value)) {
                table[idx] = new Deleted();
                size--;
                return true;
            }

            if (table[idx] == null)
                break;

            count++;
            idx++;
            if (idx == table.length)
                idx = 0;
        }

        return false;
    }

    private int hash(Object value) {
        if (value.toString().equals("Deleted"))
            return -1;

        int hash = 7;
        for (int i = 0; i < ((String)value).length(); i++) {
            hash = hash*31 + ((String)value).charAt(i);
        }
        
        return Math.abs(hash) % table.length;
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
            if (old[i] == null || hash(old[i]) == -1)
                continue;
            add((E) old[i]);
            old[i] = null;
        }
    }
}
