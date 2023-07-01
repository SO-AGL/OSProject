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
            var process = ready.remove();

            if (process.hasNextLine()) {
                var line = process.getNextLine();

                try {
                    executing = process;
                    passQuantum();
                    executing = null;

                    if (line.getBlockFor() > 0) {
                        process.setBlockedFor(line.getBlockFor());
                        blocked.add(process);
                    } else {
                        ready.add(process);
                    }
                } catch (InterruptedException ie) { }
            } else {
                finished.add(process);
            }
        } catch (NoSuchElementException e) {
            try {
                passQuantum();
            } catch (InterruptedException ie) { }
        }
    }
}
