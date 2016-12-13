package ru.mail.polis;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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

        if (root == null)
            return null;

        Node p = root;
        while (p.left != null) {
            p = p.left;
        }

        return p.value;
    }

    @Override
    public E last() {

        if (root == null)
            return null;

        Node p = root;
        while (p.right != null) {
            p = p.right;
        }

        return p.value;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new LinkedList<E>();
        addToList(list, root);
        return list;
    }

    private void addToList(List<E> list, Node node){
        if (node.left != null)
            addToList(list, node.left);
        list.add(node.value);
        if (node.right != null)
            addToList(list, node.right);
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
        Node p = root;

        while (p != null) {
            if (p.value.compareTo(value) == 0)
                return true;
            else if (p.value.compareTo(value) < 0) {
                p = p.left;
            } else
                p = p.right;
        }

        return false;
    }

    @Override
    public boolean add(E value) {
        root = add(root, value, null);
        size++;
        return true;
    }

    private Node add(Node node, E value, Node father) {
        if (node == null) {
            Node newnode = new Node(value, father);
            return newnode;
        }
        int compareResult = value.compareTo(node.value);
        if (compareResult > 0) {
            node.right = add(node.right, value, node);
            node.height = height(node.left, node.right) + 1;
        } else if (compareResult < 0) {
            node.left = add(node.left, value, node);
            node.height = height(node.left, node.right) + 1;
        } else {
            node.value = value;
        }
        node.balance = balance(node.left, node.right);
        if (node.balance == -2) {
            node = leftRotation(node);
        } else if (node.balance == 2) {
            node = rightRotation(node);
        }
        return node;
    }

    private int height(Node x, Node y) {
        if (x == null && y == null)
            return 0;
        else if (x == null)
            return y.height;
        else if (y == null)
            return x.height;
        else
            return Math.max(x.height, y.height);
    }

    private int balance(Node x, Node y) {
        if (x == null && y == null)
            return 0;
        else if (x == null)
            return -y.height;
        else if (y == null)
            return x.height;
        else
            return x.height - y.height;
    }

    private Node leftRotation(Node node) {
        if (node.right.right == null && node.right.left != null) {

            node.right = rightRotation(node.right);
            node = leftRotation(node);

        } else if (node.right.left == null || node.right.left.height <= node.right.right.height) {

            Node newNode = node.right;
            newNode.father = node.father;
            node.right = newNode.left;

            if (node.right != null)
                node.right.father = node;

            node.height = height(node.left, node.right) + 1;
            node.father = newNode;
            node.balance = balance(node.left, node.right);
            newNode.left = node;
            node = newNode;
            node.balance = balance(node.left, node.right);
            node.height = height(node.left, node.right) + 1;

        } else {
            node.right = rightRotation(node.right);
            node = leftRotation(node);
        }
        return node;
    }

    private Node rightRotation(Node node) {
        if (node.left.right != null && node.left.left == null) {

            node.left = leftRotation(node.left);
            node = rightRotation(node);

        } else if (node.left.right == null || node.left.right.height <= node.left.left.height) {

            Node newNode = node.left;
            newNode.father = node.father;
            node.left = newNode.right;

            if (node.left != null)
                node.left.father = node;

            node.height = height(node.left, node.right) + 1;
            node.father = newNode;
            node.balance = balance(node.left, node.right);
            newNode.right = node;
            node = newNode;
            node.balance = balance(node.left, node.right);
            node.height = height(node.left, node.right) + 1;

        } else {
            node.left = leftRotation(node.left);
            node = rightRotation(node);
        }
        return node;
    }

    @Override
    public boolean remove(E value) {
        root = remove(root, value);
        return true;
    }

    private Node remove(Node node, E value) {

        if (node == null)
            return null;

        int compareResult = value.compareTo(node.value);
        if (compareResult > 0) {

            node.right = remove(node.right, value);

        } else if (compareResult < 0) {

            node.left = remove(node.left, value);

        } else {

            if (node.right == null && node.left == null) {
                node = null;

            } else if (node.right == null) {
                node.left.father = node.father;
                node = node.left;

            } else if (node.left == null) {
                node.right.father = node.father;
                node = node.right;

            } else {
                if (node.right.left == null) {
                    node.right.left = node.left;
                    node.right.father = node.father;
                    node.right.father = node.father;
                    node.left.father = node.right;
                    node = node.right;

                } else {
                    Node res = min(node.right);
                    node.value = res.value;
                    remove(node.right, node.value);
                }
            }
            size--;
        }
        if (node != null) {
            node.height = height(node.left, node.right) + 1;
            node.balance = balance(node.left, node.right);

            if (node.balance == -2) {
                node = leftRotation(node);

            } else if (node.balance == 2) {
                node = rightRotation(node);
            }
        }
        return node;
    }

    private Node min(Node node) {
        if (node.left == null)
            return node;

        return min(node.left);
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

}
