package ru.mail.polis;

import java.util.*;

//TODO: write code here
public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {

    public class Node {

        public E value;
        private Node left, right, father;
        private int height;
        private int balance;

        public Node(E value, Node father) {
            this.value = value;
            this.father = father;
        }
    }

    private Node root;
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty");
        }
        Node curr = root;
        while(curr.left != null){
            curr = curr.left;
        }
        return curr.value;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty");
        }
        Node curr = root;
        while(curr.right != null){
            curr = curr.right;
        }
        return curr.value;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<>(size);
        inorderTraverse(root, list);
        return list;
    }

    private void inorderTraverse(Node curr, List<E> list) {
        if (curr == null) {
            return;
        }
        inorderTraverse(curr.left, list);
        list.add(curr.value);
        inorderTraverse(curr.right, list);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (root != null) {
            Node curr = root;
            while (curr != null) {
                int cmp = compare(curr.value, value);
                if (cmp == 0)
                    return true;
                else if (cmp < 0)
                    curr = curr.right;
                else
                    curr = curr.left;
            }
        }
        return false;
    }

    @Override
    public boolean add(E value) {
        if (root == null)
            root = new Node(value, null);
        else {
            Node n = root;
            Node father;
            while (true) {
                int cmp = compare(n.value, value);
                if (cmp == 0)
                    return false;

                father = n;

                boolean goLeft = cmp > 0;
                n = goLeft ? n.left : n.right;

                if (n == null) {
                    if (goLeft) {
                        father.left = new Node(value, father);
                    } else {
                        father.right = new Node(value, father);
                    }
                    rebalance(father);
                    break;
                }
            }
        }
        size++;
        return true;
    }

    private int height(Node n) {
        if (n == null)
            return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    private void setBalance(Node... nodes){
        for (Node n : nodes) {
            n.balance = height(n.right) - height(n.left);
        }
    }

    private Node rotateLeft(Node a){
        Node b = a.right;
        b.father = a.father;
        a.right = b.left;
        if (a.right != null) {
            a.right.father = a;
        }
        b.left = a;
        a.father = b;
        if (b.father != null) {
            if (b.father.right == a) {
                b.father.right = b;
            } else {
                b.father.left = b;
            }
        }
        setBalance(a, b);
        return b;
    }

    private Node rotateRight(Node a) {

        Node b = a.left;
        b.father = a.father;

        a.left = b.right;

        if (a.left != null)
            a.left.father = a;

        b.right = a;
        a.father = b;

        if (b.father != null) {
            if (b.father.right == a) {
                b.father.right = b;
            } else {
                b.father.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateLeftThenRight(Node n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Node rotateRightThenLeft(Node n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    private void rebalance(Node n) {
        setBalance(n);

        if (n.balance == -2) {
            if (height(n.left.left) >= height(n.left.right))
                n = rotateRight(n);
            else
                n = rotateLeftThenRight(n);

        } else if (n.balance == 2) {
            if (height(n.right.right) >= height(n.right.left))
                n = rotateLeft(n);
            else
                n = rotateRightThenLeft(n);
        }

        if (n.father != null) {
            rebalance(n.father);
        } else {
            root = n;
        }
    }

    @Override
    public boolean remove(E value) {
        if (root == null)
            return false;
        Node n = root;
        Node father = root;
        Node delNode = null;
        Node child = root;

        while (child != null) {
            father = n;
            n = child;
            int cmp = compare(value, n.value);
            child = cmp >= 0 ? n.right : n.left;
            if (cmp == 0)
                delNode = n;
        }

        if (delNode != null) {
            delNode.value = n.value;

            child = n.left != null ? n.left : n.right;

            if (compare(root.value, value) == 0) {
                root = child;
            } else {
                if (father.left == n) {
                    father.left = child;
                } else {
                    father.right = child;
                }
                rebalance(father);
            }
            size--;
            return true;
        }
        return false;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

}
