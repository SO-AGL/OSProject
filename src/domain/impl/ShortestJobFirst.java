package domain.impl;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import domain.api.SchedulingStrategy;

/**
 * This scheduling algorithm uses PriorityQueue for its ready queue, ordered by
 * time estimates, so that when it executes a process, it removes it from the
 * front of the queue, and executes it until the process either finishes or is
 * blocked.
 */
public class ShortestJobFirst extends SchedulingStrategy {
    private domain.impl.Process executing = null;

    public ShortestJobFirst() {
        ready = new PriorityQueue<>(10,
                (x, y) -> Integer.valueOf(x.getTimeEstimate()).compareTo(y.getTimeEstimate()));
        blocked = new PriorityQueue<>(10,
                (x, y) -> Integer.valueOf(x.getBlockedFor()).compareTo(y.getBlockedFor()));
    }

    @Override
    public void execute() {
        try {
            if (executing == null) {
                executing = ready.remove();
            }

            if (executing.hasNextLine()) {
                var line = executing.getNextLine();

                try {
                    notificationInterface.display("ShortestJobFirst:\nExecuting: " + executing.getName());
                    passQuantum();

                    if (line.getBlockFor() > 0) {
                        executing.setBlockedFor(line.getBlockFor());
                        blocked.add(executing);
                        executing = null;
                        return;
                    }
                } catch (InterruptedException ie) {
                    return;
                }
            }

            if (!executing.hasNextLine() && executing.getBlockedFor() == 0) {
                finished.add(executing);
                executing = null;
            }

        } catch (NoSuchElementException e) {

            try {
                passQuantum();
                return;
            } catch (InterruptedException ie) {
                return;
            }

        }

    }

    @Override
    public boolean isExecutingProcess() {
        return executing != null;
    }

}
