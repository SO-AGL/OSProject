package domain.api;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public abstract class SchedulingStrategy {
    public Queue<domain.impl.Process> ready;
    public Queue<domain.impl.Process> blocked;
    public Queue<domain.impl.Process> finished = new ArrayDeque<>();

    protected int quantumSizeMs;
    protected NotificationInterface notificationInterface;

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
    }

    public abstract void execute();

    public void setQuantumSizeMs(int size) {
        quantumSizeMs = size;
    }

    /**
     * Method to simulate the passing of a quantum of time. It will decrement
     * the blocked time of all processes in the blocked queue and add them to
     * the ready queue if they are finished.
     *
     * @throws InterruptedException
     */
    protected void passQuantum() throws InterruptedException {
        Thread.sleep(quantumSizeMs);
        decrementBlockedTimes();
    }

    private void decrementBlockedTimes() {
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
