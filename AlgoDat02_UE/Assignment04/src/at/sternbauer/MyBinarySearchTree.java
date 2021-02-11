package at.sternbauer;

public class MyBinarySearchTree implements BinarySearchTree {
    private int size = 0;
    private BinaryTreeNode root = null;

    /**
     * PLEASE NOTE: THIS METHOD WAS INSERTED TO MAKE IT EASIER FOR THE TUTOR TO TEST
     * HOWEVER, FOR THE REST VERSION 1 OF THE ASSIGNMENT WAS WORKED OUT!
     * @return root of the binary search tree
     * Returns the root of the binary search tree, or null if the tree is empty.
     */
    @Override
    public BinaryTreeNode getRoot() {
        return root;
    }


    @Override
    public boolean insert(Integer key, String elem) throws IllegalArgumentException {
        if (key == null || elem == null) throw new IllegalArgumentException("key and elem must not be null");

        BinaryTreeNode newNode = new BinaryTreeNode(key, elem);
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }

        BinaryTreeNode insertPlace = findInsertNode(key);
        if (insertPlace.key.compareTo(key) != 0) {
            if (insertPlace.key.compareTo(key) > 0) {
                insertPlace.left = newNode;
            } else {
                insertPlace.right = newNode;
            }
            size++;
            return true;
        }

        return false;
    }

    @Override
    public String find(Integer key) throws IllegalArgumentException {
        if (key == null) throw new IllegalArgumentException("Key element must not be null");
        BinaryTreeNode n = findNode(key);
        return (n != null) ? n.elem : null;
    }

    @Override
    public boolean remove(Integer key) throws IllegalArgumentException {
        if (key == null) throw new IllegalArgumentException("Key element must not be null");

        BinaryTreeNode n = findNode(key);
        if (n == null || root == null) { // No node found with key or tree empty
            return false;
        }

        //If element has no children, can be safely removed without warning -> Case 1
        if (isExternal(n.key)) {
            Integer parentKey = getParent(key);
            if (parentKey == null) { //No parent = Node is root
                root = null;
                size--;
                return true;
            }

            BinaryTreeNode parent = findNode(parentKey);
            if (parent.key.compareTo(n.key) > 0) {
                parent.left = null;
            } else {
                parent.right = null;
            }
            size--;
            return true;
        }

        //Left child node will replace n -> Case 2
        if (n.left != null && n.right == null) {
            Integer parentKey = getParent(n.key);
            if (parentKey == null) { //Node has no parent --> root
                root = n.left;
                size--;
                return true;
            }
            BinaryTreeNode parent = findNode(parentKey);
            if (parent.key.compareTo(n.key) > 0) { //Check which child is the to be removed element
                parent.left = n.left;
            } else {
                parent.right = n.left;
            }
            size--;
            return true;
        }
        //Right child node will replace n -> Case 2
        if (n.left == null && n.right != null) {
            Integer parentKey = getParent(n.key);
            if (parentKey == null) { //Node has no parent --> root
                root = n.right;
                size--;
                return true;
            }
            BinaryTreeNode parent = findNode(parentKey);
            if (parent.key.compareTo(n.key) > 0) { //Check which child is the to be removed element
                parent.left = n.right;
            } else {
                parent.right = n.right;
            }
            size--;
            return true;
        }

        //Once the code is here, it can be assumed that there are 2 child nodes
        /* Idea: Find child node somewhere in right subtree that has maximum one 1 child node
         * and has the minimal value of any nodes in the right subtree. Guaranteed to be bigger than anything
         * in the left subtree of n but also smaller than anything in the right subtree -> Perfect for Binary search tree.
         * Copy that node, remove at original position and insert at new position.
         */
        Integer minRightChildren = findMinumumValue(n.right, Integer.MAX_VALUE);
        BinaryTreeNode minRightNode = findNode(minRightChildren);
        // 1) Remove minNode from current tree
        remove(minRightChildren);
        // 2) Insert reference into new tree without the duplicate
        Integer parentKey = getParent(n.key);
        if (parentKey != null) {    //Check if to to be remove node not root --> Has parent
            BinaryTreeNode parent = findNode(parentKey);
            if (parent.key.compareTo(n.key) > 0) {
                parent.left = minRightNode;
            } else {
                parent.right = minRightNode;
            }
        } else {  //Set new root
            root = minRightNode;
        }
        //Set references to child nodes
        minRightNode.left = n.left;
        minRightNode.right = n.right;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArrayPostOrder() {
        if (root == null) {
            return new Object[0];
        }
        Object[] array = new Object[size];
        toArrayPostOrder (array, 0, root);
        return array;
    }

    @Override
    public Object[] toArrayInOrder() {
        if (root == null) {
            return new Object[0];
        }
        Object[] array = new Object[size];
        toArrayInOrder(array, 0, root);
        return array;
    }

    @Override
    public Object[] toArrayPreOrder() {
        if (root == null) {
            return new Object[0];
        }
        Object[] array = new Object[size];
        toArrayPreOrder(array, 0, root);
        return array;
    }

    @Override
    public Integer getParent(Integer key) throws IllegalArgumentException {
        if (key == null) throw new IllegalArgumentException("Key element must not be null");
        if (findNode(key) == null) return null;

        BinaryTreeNode cur = root;
        BinaryTreeNode par = null;
        while (cur != null && cur.key.compareTo(key) != 0) {
            par = cur;
            if (cur.key.compareTo(key) > 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return (par != null) ? par.key : null;
    }

    @Override
    public boolean isRoot(Integer key) throws IllegalArgumentException {
        if (key == null) throw new IllegalArgumentException("Key element must not be null");
        return (root != null && root.key.compareTo(key) == 0);
    }

    @Override
    public boolean isInternal(Integer key) throws IllegalArgumentException {
        if (key == null) throw new IllegalArgumentException("Key element must not be null");
        BinaryTreeNode node = findNode(key);
        if (node == null) return false;
        return (node.left != null || node.right != null);
    }

    @Override
    public boolean isExternal(Integer key) throws IllegalArgumentException {
        if (key == null) throw new IllegalArgumentException("Key element must not be null");
        BinaryTreeNode node = findNode(key);
        if (node == null) return false;
        return (node.left == null && node.right == null);
    }

    private int toArrayPreOrder (Object[] ret, int offset, BinaryTreeNode n) {
        ret[offset] = n.elem;
        offset++;

        if (n.left != null) {
            offset = toArrayPreOrder(ret, offset, n.left);
        }
        if (n.right != null) {
            offset = toArrayPreOrder(ret, offset, n.right);
        }
        return offset;
    }

    private int toArrayPostOrder (Object[] ret, int offset, BinaryTreeNode n) {
        if (n.left != null) {
            offset = toArrayPostOrder(ret, offset, n.left);
        }
        if (n.right != null) {
            offset = toArrayPostOrder(ret, offset, n.right);
        }
        ret[offset] = n.elem;
        offset++;
        return offset;
    }

    private int toArrayInOrder (Object[] ret, int offset, BinaryTreeNode n) {
        if (n.left != null) {
            offset = toArrayInOrder(ret, offset, n.left);
        }
        ret[offset] = n.elem;
        offset++;
        if (n.right != null) {
            offset = toArrayInOrder(ret, offset, n.right);
        }

        return offset;
    }

    private BinaryTreeNode findNode (Integer key) {
        BinaryTreeNode n = root;

        while (n != null && n.key.compareTo(key) != 0) {
            if (n.key.compareTo(key) > 0) {
                n = n.left;
            } else {
                n = n.right;
            }
        }
        return n;
    }

    private BinaryTreeNode findInsertNode (Integer key) {
        BinaryTreeNode n = root;
        BinaryTreeNode prev = null;

        while (n != null && n.key.compareTo(key) != 0) {
            prev = n;
            if (n.key.compareTo(key) > 0) {
                n = n.left;
            } else {
                n = n.right;
            }
        }
        return (n != null) ? n : prev;
    }

    private Integer findMinumumValue (BinaryTreeNode n, Integer currentMin) {
        //If node has left child -> Check them as they are smaller
        if (n.left != null) {
            currentMin = findMinumumValue(n.left, currentMin);
        } else if (n.key.compareTo(currentMin) < 0) {
            currentMin = n.key; //Only right child node or no children --> Check own value
        }
        return currentMin;
    }
}