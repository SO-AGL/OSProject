package domain.impl;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

import domain.api.ControlInterface;
import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;

public class ShortTermScheduler extends Thread implements ControlInterface, InterSchedulerInterface {
    private NotificationInterface notificationInterface;

    private Queue<Process> ready = new PriorityQueue<>(10,
            (x, y) -> Integer.valueOf(x.getRemainingTime()).compareTo(y.getRemainingTime()));
    private Queue<Process> blocked = new PriorityQueue<>(10,
            (x, y) -> Integer.valueOf(x.getBlockedFor()).compareTo(y.getBlockedFor()));
    private Queue<Process> finished = new ArrayDeque<>();

    private int elapsedTime = 0;
    private int quantumSizeMs = 200;

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
    }

    public void setQuantumSizeMs(int quantumSizeMs) {
        this.quantumSizeMs = quantumSizeMs;
    }

    @Override
    public void addProcess(Process bcp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProcess'");
    }

    @Override
    public int getProcessLoad() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProcessLoad'");
    }

    @Override
    public void startSimulation() {
        start();
    }

    @Override
    public void suspendSimulation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'suspendSimulation'");
    }

    @Override
    public void resumeSimulation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resumeSimulation'");
    }

    @Override
    public void stopSimulation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopSimulation'");
    }

    @Override
    public void displayProcessQueues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayProcessQueues'");
    }

    public void run() {
        while (true) {
            try {
                var process = ready.remove();

                while (process.hasNextLine()) {
                    var line = process.getNextLine();

                    try {
                        Thread.sleep(quantumSizeMs);
                        elapsedTime++;

                        if (line.getBlockFor() > 0) {
                            process.setBlockedFor(line.getBlockFor());
                            blocked.add(process);
                            break;
                        }
                    } catch (InterruptedException ie) {
                    }
                }

                if (!process.hasNextLine() && process.getBlockedFor() == 0) {
                    finished.add(process);
                }

            } catch (NoSuchElementException e) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                }
            }
        }
    }

}
