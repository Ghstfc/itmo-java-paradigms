package queue;

public interface Queue {
    void enqueue(Object object);
    Object dequeue();
    Object element();
    int size();
    boolean isEmpty();
    void clear();
}
