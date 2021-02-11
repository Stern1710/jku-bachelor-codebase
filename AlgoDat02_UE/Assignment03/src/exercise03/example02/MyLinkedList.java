package exercise03.example02;

public class MyLinkedList {
	public MyNode head;
	public MyNode tail;
	
	public MyLinkedList() {
		head = null;
		tail = null;
	}
	
	public void clear() {head = null; tail = null;}
	
	public void add(Integer val) {
		if(head == null) {
			head = new MyNode(val);
			tail = head;
		} else {
			MyNode tmp = new MyNode(val);
			tail.next = tmp;
			tail = tail.next;
			tail.next = null;
		}
	}
	
	public void add(MyNode node) {
		if(head == null) {
			head = node;
			tail = head;
			node.next = null;
		} else {
			tail.next = node;
			tail = tail.next;
			tail.next = null;
		}
	}
	
	public void link(MyLinkedList list) {
		if (this.head == null) {
			this.head = list.head;
			this.tail = list.tail;
		} else if (list.head != null) {
			//Only merge new list if this one is not null
			this.tail.next = list.head;
			this.tail = list.tail;
		}
	}
}
