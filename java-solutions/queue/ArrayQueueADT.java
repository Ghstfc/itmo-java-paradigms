package queue;

import java.util.Arrays;
import java.util.Objects;

/*
    Model : a[1]...a[n]
            front = -1;
            rear = -1;
    Invariant : for i=1...n: a[n] != null

    Let immutable(n) : for i=1..n: a'[i] == a[i]

    Pred: object != null
    Post: n' = n + 1 && a[n'] = object && immutable
    enqueue(object)

    Pred: a.length != 0
    Post: immutable
    element

    Pred: n > 0
    Post: R = a[front] && front' = front + 1 for i=front..n a[i] = a[i+1]
    dequeue

    Pred: true
    Post: R == n && n' = n && immutable
    size

    Pred: true
    Post: R = (n == 0) && n' = n && immutable
    isEmpty

    Pred: true
    Post: for i=1..n a[i] = null && n' = 0
    clear


    Pred: true
    Post: R = \sum 1->n : 1 * [ a[i] == key ]
    count(key)
    */
public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int front = 0;
    private int rear = 0;
    private int size = 0;
    private int right = 0;


    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue);
        queue.elements[queue.rear++] = element;
        queue.size++;
    }

    private static void ensureCapacity(final ArrayQueueADT queue) {
        if (queue.rear == queue.elements.length && queue.front == 0) {
            queue.elements = Arrays.copyOf(queue.elements, queue.elements.length * 2);
        } else if (queue.rear == queue.elements.length) {
            queue.right = queue.rear;
            queue.rear = (queue.rear + 1) % queue.elements.length - 1;
        } else if (queue.front == queue.rear && queue.front != 0) {
            Object[] tmp = new Object[queue.rear];
            System.arraycopy(queue.elements, 0, tmp, 0, queue.rear);
            System.arraycopy(queue.elements, queue.front, queue.elements, 0, queue.elements.length - queue.rear);
            System.arraycopy(tmp, 0, queue.elements, queue.elements.length - tmp.length, tmp.length);
            queue.elements = Arrays.copyOf(queue.elements, queue.elements.length * 2);
            queue.front = 0;
            queue.rear = queue.right;

        }    }

    public static Object element(final ArrayQueueADT queue) {
        return queue.elements[queue.front];
    }

    public static Object dequeue(final ArrayQueueADT queue) {
        Object tmp = queue.elements[queue.front];
        queue.elements[queue.front++] = null;
        queue.size--;
        if (queue.size == 0) {
            queue.front = 0;
            queue.rear = 0;
        }
        if (queue.size > 0 && queue.front >= queue.elements.length ) {
            queue.front = 0;
        }
        return tmp;
    }

    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    public static void clear(final ArrayQueueADT queue) {
        Arrays.fill(queue.elements, null);
        queue.size = 0;
        queue.front = 0;
        queue.rear = 0;
    }

    public static void get(final ArrayQueueADT queue) {
        System.out.println(Arrays.toString(queue.elements));
    }

    public static int count( final ArrayQueueADT queue, Object x) {
        int count = 0;
        for (int i = 0; i < queue.size; i++) {
            if (queue.elements[(queue.front + i)% queue.elements.length].equals(x)) {
                count++;
            }
        }
        return count;
    }

}