package domain.impl;

import java.util.ArrayDeque;

import domain.api.SchedulingStrategy;

public class RoundRobin extends SchedulingStrategy {
    public RoundRobin() {
        ready = new ArrayDeque<>();
        blocked = new ArrayDeque<>();
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(quantumSizeMs);
        } catch (Exception e) {
            throw new Error(e);
        }

        decrementBlockedTimes();

        try {
            var readyProcess = ready.remove();

            if (readyProcess.hasNextLine()) {
                var line = readyProcess.getNextLine();

                if (line.getBlockFor() > 0) {
                    readyProcess.setBlockedFor(line.getBlockFor());
                    blocked.add(readyProcess);
                } else {
                    ready.add(readyProcess);
                }
            } else {
                finished.add(readyProcess);
            }

        } catch (NoSuchElementException e) { }
    }

    /**
     * Decrements the blocked time of each process in blocked queue. If a process reaches block time of zero,
     * it is removed from the blocked queue and added to the ready queue.
     */
    private void decrementBlockedTimes() {
        for (var blockedProcess : blocked) {
            blockedProcess.decrementBlockedTime();
            if (blockedProcess.getBlockedFor() == 0) {
                blocked.remove(blockedProcess);
                ready.add(blockedProcess);
            }
        }
    }
}
