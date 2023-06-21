package domain.impl;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import domain.api.SchedulingStrategy;

public class ShortestJobFirst extends SchedulingStrategy {
    public ShortestJobFirst() {
        ready = new PriorityQueue<>(10,
            (x, y) -> Integer.valueOf(x.getRemainingTime()).compareTo(y.getRemainingTime()));
        blocked = new PriorityQueue<>(10,
            (x, y) -> Integer.valueOf(x.getBlockedFor()).compareTo(y.getBlockedFor()));
    }

    private int quantumSizeMs = 0;

    @Override
    public void execute() {
        
        try {
            var process = ready.remove();

            while (process.hasNextLine()) {
                var line = process.getNextLine();

                try {
                    Thread.sleep(quantumSizeMs);

                    if (line.getBlockFor() > 0) {
                        process.setBlockedFor(line.getBlockFor());
                        blocked.add(process);
                        return;
                    }
                } catch (InterruptedException ie) {
                    return;
                }
            }

            if (!process.hasNextLine() && process.getBlockedFor() == 0) {
                finished.add(process);
            }

        } catch (NoSuchElementException e) {
            
            try {
                Thread.sleep(quantumSizeMs);
                return;
            } catch (InterruptedException ie) {
                return;
            }

        }

    }

    @Override
    public void setQuantumSizeMs(int size) {
        quantumSizeMs = size;
    }

}
