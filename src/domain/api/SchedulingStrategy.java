package domain.api;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public abstract class SchedulingStrategy {
    public Queue<domain.impl.Process> ready;
    public Queue<domain.impl.Process> blocked;
    public Queue<domain.impl.Process> finished = new ArrayDeque<>();

    protected int quantumSizeMs; 

    public abstract void execute();

    public void setQuantumSizeMs(int size) {
        quantumSizeMs = size;
    }

    protected void decrementBlockedTimes() {
        var toRemove = new ArrayList<>();
        for (var blockedProcess : blocked) {
            blockedProcess.decrementBlockedTime();
            if (blockedProcess.getBlockedFor() == 0) {
                ready.add(blockedProcess);
                toRemove.add(blockedProcess);
            }
        }

        blocked.removeAll(toRemove);
    }
}
