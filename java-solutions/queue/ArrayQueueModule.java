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
public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int front = 0;
    private static int rear = 0;
    private static int size = 0;
    private static int right = 0;


    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[rear++] = element;
        size++;
    }

    private static void ensureCapacity() {
        if (rear == elements.length && front == 0) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        } else if (rear == elements.length) {
            right = rear;
            rear = (rear + 1) % elements.length - 1;
        } else if (front == rear && front != 0) {
            Object[] tmp = new Object[rear];
            System.arraycopy(elements, 0, tmp, 0, rear);
            System.arraycopy(elements, front, elements, 0, elements.length - rear);
            System.arraycopy(tmp, 0, elements, elements.length - tmp.length, tmp.length);
            elements = Arrays.copyOf(elements, elements.length * 2);
            front = 0;
            rear = right;

        }
    }

    public static Object element() {
        return elements[front];
    }

    public static Object dequeue() {
        Object tmp = elements[front];
        elements[front++] = null;
        size--;
        if (size == 0) {
            front = 0;
            rear = 0;
        }
        if (size > 0 && front >= elements.length) {
            front = 0;
        }
        return tmp;
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size == 0;
    }

    public static void clear() {
        Arrays.fill(elements, null);
        size = 0;
        front = 0;
        rear = 0;
    }

    public static void get() {
        System.out.println(Arrays.toString(elements));
    }

    public static int count(Object x) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (elements[(front + i)% elements.length].equals(x)) {
                count++;
            }
        }
        return count;
    }

}