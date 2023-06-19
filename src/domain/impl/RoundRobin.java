package domain.impl;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import domain.api.SchedulingStrategy;

public class RoundRobin extends SchedulingStrategy {
    public RoundRobin() {
        ready = new ArrayDeque<>();
        blocked = new ArrayDeque<>();
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public void setQuantumSizeMs(int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setQuantumSizeMs'");
    }
}
