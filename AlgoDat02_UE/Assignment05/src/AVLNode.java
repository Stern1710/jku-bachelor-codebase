
public class AVLNode {
	public AVLNode parent = null;
	public AVLNode left = null;
	public AVLNode right = null;
	
	public Integer key;
	public String elem;
	
	public int height = 0; // To determine node height in O(1)

	public AVLNode(Integer key,  String elem) {
		this.key = key;
		this.elem = elem;
	}

	public AVLNode(Integer key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key + " " + elem;
	}
}

