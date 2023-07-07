package utils;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.ArrayList;

public class PriorityQueue<T, S extends Comparable<S>> extends AbstractQueue<T> {

    private List<T> queue = new ArrayList<>();
    private Function<T, S> getter;

    public PriorityQueue(Function<T, S> getter) {
        super();
    
        this.getter = getter;
    }

    @Override
    public boolean offer(T e) {
        if (e == null) {
            throw new NullPointerException("Null element");
        }

        int index = Utils.upperBound(queue, e, getter);

        queue.add(Math.max(0, index), e);

        return true;
    }
    

    @Override
    public T poll() {
        if (queue.isEmpty()) {
            return null;
        }

        return queue.remove(0);
    }
    

    @Override
    public T peek() {
        if (queue.isEmpty()) {
            return null;
        }

        return queue.get(0);
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }
    
}
