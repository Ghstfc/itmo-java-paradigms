package queue;


public class LinkedQueue extends AbstractQueue{
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
        if (size == 0){
            head = tail;
        }else {
            perm.next = tail;
        }
    }

    public Object dequeueImpl() {
        Object element = head.element;
        head = head.next;
        return element;
    }

    public Object element(){
        assert size > 0;
        return head.element;
    }

    public void clear(){
        head = null;
        tail = null;
        size = 0;
    }










}
