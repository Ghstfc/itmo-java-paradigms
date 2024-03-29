package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int front = 0;
    private int rear = 0;
    private int right = 0;



    public ArrayQueue() {
        elements = new Object[2];
    }

    protected void enqueueImpl(Object element) {
        Objects.requireNonNull(element);
        // :NOTE: не нужно передавать this в качестве аргумента, это абсолютно не нужно
        ensureCapacity(this);
        this.elements[this.rear++] = element;

    }

    private void ensureCapacity(ArrayQueue queue) {
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

        }
    }

    public Object element(ArrayQueue this) {
        return this.elements[this.front];
    }

    public Object dequeueImpl(ArrayQueue this) {
        Object tmp = this.elements[this.front];
        this.elements[this.front++] = null;

        if (this.size == 0) {
            this.front = 0;
            this.rear = 0;
        }
        if (this.size > 0 && this.front >= this.elements.length) {
            this.front = 0;
        }
        return tmp;
    }

    // :NOTE: аналогично комментарию выше, можно не писать this и обращаться к полям без this
    public void clear(ArrayQueue this) {
        Arrays.fill(this.elements, null);
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    public void get(ArrayQueue this) {
        System.out.println(Arrays.toString(this.elements));
    }

    public int count(ArrayQueue this, Object x) {
        int count = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.elements[(this.front + i) % this.elements.length].equals(x)) {
                count++;
            }
        }
        return count;
    }

    // :NOTE: дублирование кода с count
    @Override
    protected int countIfimpl(Predicate<Object> predicate) {
        int count = 0;
        for (int i = 0; i < this.size; i++) {
            if (predicate.test(this.elements[(this.front + i) % this.elements.length])) {
                count++;
            }
        }
        return count;
    }
}