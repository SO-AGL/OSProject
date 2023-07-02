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
    public ShortestJobFirst() {
        ready = new PriorityQueue<>(10,
                (x, y) -> Integer.valueOf(x.getTimeEstimate()).compareTo(y.getTimeEstimate()));
        blocked = new PriorityQueue<>(10,
                (x, y) -> Integer.valueOf(x.getBlockedFor()).compareTo(y.getBlockedFor()));
    }

    @Override
    public void execute() {
        try {
            var process = ready.remove();

            while (process.hasNextLine()) {
                var line = process.getNextLine();

                try {
                    notificationInterface.display("ShortestJobFirst: \nExecuting: " + process.getName());
                    passQuantum();

                    if (line.getBlockFor() > 0) {
                        process.setBlockedFor(line.getBlockFor());
                        blocked.add(process);
                        return;
                    }
                } catch (InterruptedException ie) {
                    return;
                }
            }

            if (process.getBlockedFor() == 0) {
                finished.add(process);
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

}
