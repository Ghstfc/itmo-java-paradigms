package queue;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {

    protected int size = 0;


    @Override
    public void enqueue(Object object) {
        Objects.requireNonNull(object);
        enqueueImpl(object);
        size++;
    }

    protected abstract void enqueueImpl(Object obj);


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    @Override
    public int countIf(Predicate<Object> predicate) {
        return countIfimpl(predicate);
    }

    protected abstract int countIfimpl(Predicate<Object> predicate);
}
