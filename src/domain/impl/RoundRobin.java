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
}
