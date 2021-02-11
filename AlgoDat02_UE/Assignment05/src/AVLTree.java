public class AVLTree {
	private AVLNode root;
	private int size;
	private AVLNode[] helperNodes;

	/**
	 * Default constructor.
	 * Initializes the AVL tree.
	 */
	public AVLTree() {
		root = null;
		size = 0;
		helperNodes = new AVLNode[7];
	}

	/*
	 * @return the root node of the AVL tree
	 */
	public AVLNode getRoot() {
		return root;
	}

	/**
	 * Retrieves tree height.
	 * @return -1 in case of empty tree, current tree height otherwise.
	 */
	public int height() {
		return root == null ? -1 : root.height;
	}

	/**
	 * Inserts a new node into AVL tree.
	 * @param key Key of the new node. May not be null. Elements with the same key are not allowed,
	 * in this case false is returned.
	 * @param elem Data of the new node. May not be null.
	 * @return true if insert was successful, false otherwise.
	 */
	public boolean insert (Integer key, String elem) throws IllegalArgumentException {
		if (key == null || elem == null) throw new IllegalArgumentException("key and elem must not be null");

		AVLNode newNode = new AVLNode(key, elem);
		if (root == null) {
			root = newNode;
			size++;
			return true;
		}

		AVLNode parent = findInsertNode(key);
		if (parent.key.compareTo(newNode.key) == 0) {
			return false;
		}

		if(parent.key.compareTo(newNode.key) > 0) {
			parent.left = newNode;
			newNode.parent = parent;
		} else {
			parent.right = newNode;
			newNode.parent = parent;
		}

		updateHeights(parent);
		while (newNode.parent != null &&
				newNode.parent.parent != null &&
				Math.abs(getNodeHeight(newNode.parent.parent.left) - getNodeHeight(newNode.parent.parent.right)) <= 1) {
			newNode = newNode.parent;
		}
		restructure(newNode); //Restructure tree starting at new node

		size++;
		return true;
	}

	/**
	 * Removes the first node with given key.
	 * @param key Key of node to remove. May not be null.
	 * @return true, if element was found and deleted.
	 */
	public boolean remove (Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Key element must not be null");

		AVLNode n = findNode(key);
		if (n == null || root == null) { // No node found with key or tree empty
			return false;
		}

		//Case 1: n has no children -> can be safely removed
		if (isExternal(n.key)) {
			if (n.parent == null) { //n has no parent -> is root
				root = null;
			} else {
				if (n.parent.left == n) {
					n.parent.left = null;
				} else {
					n.parent.right = null;
				}
				updateHeights(n.parent);
			}
		//Case 2: n has one children (here right)
		} else if (n.left == null && n.right != null) {
			if (n.parent == null) { //n has one child and is root
				root = n.right;
				n.right.parent = null;
				updateHeights(n.right);
			} else {
				n.right.parent = n.parent;
				if (n.parent.left == n) {
					n.parent.left = n.right;
				} else {
					n.parent.right = n.right;
				}
				updateHeights(n.right);
			}
		//Case 2: n has one children (here left)
		} else if (n.left != null && n.right == null) {
			if (n.parent == null) { //n has one child and is root
				root = n.left;
				n.left.parent = null;
				updateHeights(n.left);
			} else {
				if (n.parent.left == n) {
					n.parent.left = n.left;
				} else {
					n.parent.right = n.left;
				}
				n.left.parent = n.parent;
				updateHeights(n.left);
			}
		//Case 3: n has two children, search for inOrder successor
		} else {
			AVLNode successor = getInOrderSuccessor(n);
			n.key = successor.key;
			n.elem = successor.elem;

			if (successor.parent == n) {
				n.right = successor.right;
				if (successor.right != null) {
					successor.right.parent = n;
				}
			} else {
				successor.parent.left = successor.right;
				if (successor.right != null) {
					successor.right.parent = successor.parent;
				}
			}

			updateHeights(successor.parent);
			updateHeights(n);
		}

		isAVLTree(root);
		size--;
		return true;
	}

	/**
	 * Returns value of a first found node with given key.
	 * @param key Key to search. May not be null.
	 * @return Corresponding value if key was found, null otherwise.
	 */
	public String find (Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Key element must not be null");
		AVLNode n = findNode(key);
		return (n != null) ? n.elem : null;
	}

	/**
	 * Returns the number of key/value pairs in the tree.
	 * @return Number of key/value pairs.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns an array representation of the data elements (pre-ordered).
	 * @return Array representation of the tree.
	 */
	public Object[] toArray() {
		String[] array = new String[size];
		if (size > 0) { toArrayPreOrder(array, 0, root); }
		return array;
	}

	private int toArrayPreOrder (Object[] array, int offset, AVLNode n) {
		array[offset] = n.elem;
		offset++;

		if (n.left != null) {
			offset = toArrayPreOrder(array, offset, n.left);
		}
		if (n.right != null) {
			offset = toArrayPreOrder(array, offset, n.right);
		}
		return offset;
	}

	/**
	 * Returns height of node or -1 or node is not there (child of a leave)
	 * @param n Node where the height should be returned
	 * @return Height of node or -1 if node does not exist
	 */
	private static int getNodeHeight(AVLNode n) {
		return n == null ? -1 : n.height;
	}

	/**
	 * Updates node heights starting from given node.
	 * @param n Node to start update height operation with.
	 */
	private void updateHeights(AVLNode n) {
		for (; n != null; n = n.parent) {
			n.height = Math.max(getNodeHeight(n.left), getNodeHeight(n.right)) + 1;
		}
	}

	/**
	 * Implements cut & link restructure operation.
	 * @param node Node to start restructuring with.
	 */
	private void restructure(AVLNode node) {
		if (node == null || node.parent == null || node.parent.parent == null) {
			return;
		}

		helperNodes = new AVLNode[7];
		AVLNode kid = node.parent;
		AVLNode parent = kid.parent;
		AVLNode grandparent = parent.parent;

		AVLNode[] abc = getABC(parent, kid, node);

		helperNodes[4] = abc[0];
		helperNodes[5] = abc[1];
		helperNodes[6] = abc[2];
		helperNodes[0] = helperNodes[4].left;
		helperNodes[3] = helperNodes[6].right;

		if (helperNodes[5] == kid) {
			if (node.key.compareTo(parent.key) < 0) {
				helperNodes[1] = helperNodes[4].right;
				helperNodes[2] = helperNodes[5].right;
			} else {
				helperNodes[1] = helperNodes[5].left;
				helperNodes[2] = helperNodes[6].left;
			}
		} else {
			helperNodes[1] = node.left;
			helperNodes[2] = node.right;
		}

		cutAndLink(parent, grandparent);
		helperNodes[6].height = Math.max(getNodeHeight(helperNodes[6].left), getNodeHeight(helperNodes[6].right)) + 1;
		helperNodes[4].height = Math.max(getNodeHeight(helperNodes[4].left), getNodeHeight(helperNodes[4].right)) + 1;
		helperNodes[5].height = Math.max(helperNodes[4].height, helperNodes[6].height) + 1;
		updateHeights(helperNodes[5].parent);
	}

	private static AVLNode[] getABC(AVLNode x, AVLNode y, AVLNode z) {
		AVLNode[] abc = new AVLNode[3];

		if (x.key.compareTo(y.key) > 0) {
			if (x.key.compareTo(z.key) > 0) {
				if (y.key.compareTo(z.key) > 0) {
					abc[0] = z;
					abc[1] = y;
				} else {
					abc[0] = y;
					abc[1] = z;
				}
				abc[2] = x;
			} else {
				abc[0] = y;
				abc[1] = x;
				abc[2] = z;
			}
		} else if (y.key.compareTo(z.key) > 0) {
			if (x.key.compareTo(z.key) > 0) {
				abc[0] = z;
				abc[1] = x;
			} else {
				abc[0] = x;
				abc[1] = z;
			}
			abc[2] = y;
		} else {
			abc[0] = x;
			abc[1] = y;
			abc[2] = z;
		}

		return abc;
	}

	private void cutAndLink(AVLNode parent, AVLNode grandparent) {
		if (parent == root || grandparent == null) {
			root = helperNodes[5];
		}

		helperNodes[6].parent = helperNodes[5];
		helperNodes[5].right = helperNodes[6];
		helperNodes[4].parent = helperNodes[5];
		helperNodes[5].left = helperNodes[4];

		if (helperNodes[0] != null) {
			helperNodes[0].parent = helperNodes[4];
		}
		helperNodes[4].left = helperNodes[0];

		if (helperNodes[1] != null) {
			helperNodes[1].parent = helperNodes[4];
		}
		helperNodes[4].right = helperNodes[1];


		if (helperNodes[2] != null) {
			helperNodes[2].parent = helperNodes[6];
		}
		helperNodes[6].left = helperNodes[2];

		if (helperNodes[3] != null) {
			helperNodes[3].parent = helperNodes[6];
		}
		helperNodes[6].right = helperNodes[3];

		helperNodes[5].parent = grandparent;
		if (grandparent != null) {
			if (parent == grandparent.left) {
				grandparent.left = helperNodes[5];
			} else {
				grandparent.right = helperNodes[5];
			}
		}
	}

	private AVLNode getInOrderSuccessor(AVLNode n) {
		if (n == null) return null;

		//in ordner --> left root Right
		//Therefore after the root the right and its left nodes have to be searched
		if (n.right != null) {
			AVLNode successor;
			for (successor = n.right; successor.left != null; successor = successor.left);
			return successor;
		} else {
			//if no right node, search for parent that is in order successor
			//(first node where child below searched is not the right child)
			AVLNode parent;
			for (parent = n.parent; parent != null && parent.right == n; n = parent, parent = parent.parent);
			return parent;
		}
	}

	/**
	 * Checks AVL integrity and restructures if neccessary
	 * @param n Node to check integrity for.
	 * @return true If AVL integrity is sane, false otherwise.
	 */
	private boolean isAVLTree(AVLNode n) {
		if (n == null) return false;

		if (Math.abs(getNodeHeight(n.left) - getNodeHeight(n.right)) > 1) {
			AVLNode kid, grandkid = null;

			kid = getNodeHeight(n.left) >= getNodeHeight(n.right) ? n.left : n.right;
			if (kid != null) {
				grandkid = getNodeHeight(kid.left) >= getNodeHeight(kid.right) ? kid.left : kid.right;
			}

			restructure(grandkid);
			return true;
		} else {
			return isAVLTree(n.left) || isAVLTree(n.right);
		}
	}

	private AVLNode findInsertNode (Integer key) {
		AVLNode n = root;
		AVLNode prev = null;

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

	private AVLNode findNode (Integer key) {
		AVLNode n = root;

		while (n != null && n.key.compareTo(key) != 0) {
			if (n.key.compareTo(key) > 0) {
				n = n.left;
			} else {
				n = n.right;
			}
		}
		return n;
	}

	private boolean isExternal(Integer key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("Key element must not be null");
		AVLNode node = findNode(key);
		if (node == null) return false;
		return (node.left == null && node.right == null);
	}
}