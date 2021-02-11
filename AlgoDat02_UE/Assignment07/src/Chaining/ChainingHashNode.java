package Chaining;

public class ChainingHashNode {
	public Integer key;
	public String data;
	public ChainingHashNode next;

	ChainingHashNode(Integer key, String data) {
		this.key = key;
		this.data = data;
		next = null;
	}
}
