package utilities;

public class PriorityQueue<T> {
	private PriorityNode<T> head;

	public PriorityQueue() {
	}

	private int size() {
		// If there is no head, then the queue is empty
		if (head == null) {
			return 0;
		}

		int size = 1;

		// Go through the queue until you reach the last item
		PriorityNode<T> nextNode = head;
		while (nextNode.getNext() != null) {
			nextNode = nextNode.getNext();
			size++;
		}

		return size;
	}

	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}

		return false;
	}

	public void enqueue(T item, int priority) {
		// Make sure the priority is within bounds
		if (priority < 1 || priority > 10) {
			return;
		}

		if (head == null) {
			head = new PriorityNode<T>(item, null, priority);
			return;
		}

		// Go through the queue until you reach the correct spot
		PriorityNode<T> nextNode = head;
		while (nextNode.getNext() != null && nextNode.getNext().getPriority() < priority) {
			nextNode = nextNode.getNext();
		}

		// Put the item into its place in the queue
		if (nextNode.getNext() != null) {
			nextNode.setNext(new PriorityNode<T>(item, nextNode.getNext(), priority));
		} else {
			nextNode.setNext(new PriorityNode<T>(item, null, priority));
		}
	}

	public T dequeue() {
		// If the queue is empty, don't remove anything
		if (isEmpty()) {
			return null;
		}

		int size = size();

		if (size == 1) {
			T itemOut = head.getData();

			head = null;

			return itemOut;
		}

		// Go to the second last item
		PriorityNode<T> nextNode = head;
		for (int node = 0; node < size - 2; node++) {
			nextNode = nextNode.getNext();
		}

		// Save the data of the last item
		T itemOut = nextNode.getNext().getData();

		// Remove the last item
		nextNode.setNext(null);

		return itemOut;
	}
}