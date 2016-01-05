package utilities;

public class PriorityNode<T> {
	private T data;
	private PriorityNode<T> next;
	private int priority;

	public PriorityNode(T data, PriorityNode<T> next, int priority) {
		this.data = data;
		this.next = next;
		this.priority = priority;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public PriorityNode<T> getNext() {
		return next;
	}

	public void setNext(PriorityNode<T> next) {
		this.next = next;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}