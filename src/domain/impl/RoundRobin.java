package domain.impl;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import domain.api.SchedulingStrategy;

/**
 * This scheduling strategy uses ArrayDeque for its ready queue, so that
 * whenever it executes a process, it removes it from the front of ready queue,
 * executes it for a quantum of time, then puts it at the end of the ready or
 * blocked queue, according to the last instruction.
 */
public class RoundRobin extends SchedulingStrategy {
    private boolean _isExecutingProcess = false;

    /**
     * Instantiates a new `RoundRobin` that extends `SchedulingStrategy` with
     * ready and blocked queues as new `ArrayDeque`s.
     */
    public RoundRobin() {
        ready = new ArrayDeque<>();
        blocked = new ArrayDeque<>();
    }

    /**
     * Gets the first process in the ready queue and simulates the execution of
     * one instruction of the process, displaying the process beign executed,
     * passing the time of a quantum, and then adding it in the ready, blocked
     * or finished queue, depending on the instruction read. If no process gets
     * executed, just passes one quantum.
     */
    @Override
    public void execute() {
        try {
            var process = ready.remove();
            _isExecutingProcess = true;

            if (process.hasNextLine()) {
                var line = process.getNextLine();

                try {
                    notificationInterface.display("RoundRobin: \nExecuting: " + process.getName());
                    passQuantum();

                    if (line.getBlockFor() > 0) {
                        process.setBlockedFor(line.getBlockFor());
                        blocked.add(process);
                    } else if (process.hasNextLine()) {
                        ready.add(process);
                    } else {
                        finished.add(process);
                    }
                } catch (InterruptedException ie) { }
            } else {
                finished.add(process);
            }
        } catch (NoSuchElementException e) {
            try {
                passQuantum();
            } catch (InterruptedException ie) { }
        } finally {
            _isExecutingProcess = false;
        }
    }

    /**
     * Wheter this strategy is executing an process or not.
     */
    @Override
    public boolean isExecutingProcess() {
        return _isExecutingProcess;
    }
}
