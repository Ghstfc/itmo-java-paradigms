package queue;

import java.util.function.Predicate;


/*
    Model : a[1]...a[n]
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

    Pred: predicate != null
    Post: R = (a[1]..a[n]: predicate)
    countIf()
    */

public interface Queue {
    void enqueue(Object object);
    Object dequeue();
    Object element();
    int size();
    boolean isEmpty();
    void clear();
    int countIf(Predicate<Object> predicate);
}
