package domain.impl;

import java.util.PriorityQueue;

import domain.api.SchedulingStrategy;

public class ShortestJobFirst extends SchedulingStrategy {
    public ShortestJobFirst() {
        ready = new PriorityQueue<>(10,
            (x, y) -> Integer.valueOf(x.getRemainingTime()).compareTo(y.getRemainingTime()));
        blocked = new PriorityQueue<>(10,
            (x, y) -> Integer.valueOf(x.getBlockedFor()).compareTo(y.getBlockedFor()));
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

}
