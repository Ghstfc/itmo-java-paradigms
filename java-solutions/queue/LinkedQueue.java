package queue;


import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private final Object element;
        private Node next;

        public Node(Object element, Node next) {
            this.element = element;
            this.next = next;
        }
    }

    private Node tail;
    private Node head;

    protected void enqueueImpl(final Object element) {

        Node perm = tail;
        tail = new Node(element, null);
        if (size == 0) {
            head = tail;
        } else {
            perm.next = tail;
        }
    }

    protected Object dequeueImpl() {
        Object element = head.element;
        head = head.next;
        return element;
    }

    public Object element() {
        assert size > 0;
        return head.element;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    protected int countIfimpl(Predicate<Object> predicate) {
        int count = 0;
        int tmp = 0;
        while (tmp < size) {
            Object perm = dequeue();
            if (predicate.test(perm)){
                count++;
            }
            enqueue(perm);
            tmp++;
        }
        return count;
    }


}
