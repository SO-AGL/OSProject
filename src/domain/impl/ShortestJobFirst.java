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

    /**
     * Intantiates a new `ShortestJobFirst` with `PriorityQueue` as ready queue.
     */
    public ShortestJobFirst() {
        ready = new PriorityQueue<>(10,
                (x, y) -> Integer.valueOf(x.getTimeEstimate()).compareTo(y.getTimeEstimate()));
        blocked = new PriorityQueue<>(10,
                (x, y) -> Integer.valueOf(x.getBlockedFor()).compareTo(y.getBlockedFor()));
    }

    /**
     * Each time this method is called, it either removes a process from the
     * ready queue, or continues the execution of the currently executing
     * process. If the process calls a blocking instruction, it puts it in the
     * blocked queue. If the process has no more lines to execute, then it puts
     * it in the finished queue. This method always executes one `ProgramLine`
     * at a time, simulating the passing of one quantum of time.
     */
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

    /**
     * Returns `true` if there is a process currently executing, `false`
     * otherwise.
     */
    @Override
    public boolean isExecutingProcess() {
        return executing != null;
    }

}
