package domain.impl;

import java.util.stream.Collectors;

import domain.api.ControlInterface;
import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;
import domain.api.SchedulingStrategy;

public class ShortTermScheduler extends Thread implements ControlInterface, InterSchedulerInterface {
    private NotificationInterface notificationInterface;
    private SchedulingStrategy schedulingStrategy;

    private boolean isRunning = false;

    public ShortTermScheduler(SchedulingStrategy schedulingStrategy, int quantumSizeMs) {
        this.schedulingStrategy = schedulingStrategy;
        this.schedulingStrategy.setQuantumSizeMs(quantumSizeMs);
    }

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
    }

    @Override
    public void addProcess(Process process) {
        schedulingStrategy.ready.add(process);
    }

    @Override
    public int getProcessLoad() {
        return schedulingStrategy.ready.size() + schedulingStrategy.blocked.size();
    }

    @Override
    public void startSimulation() {
        isRunning = true;

        if (!isAlive()) {
            start();
        }

        notificationInterface.display("Simulation started!\n");
    }

    @Override
    public void suspendSimulation() {
        isRunning = false;

        notificationInterface.display("Simulation suspended!\n");
    }

    @Override
    public void resumeSimulation() {
        isRunning = true;

        notificationInterface.display("Simulation resumed!\n");
    }

    @Override
    public void stopSimulation() {
        isRunning = false;
        restart();

        notificationInterface.display("Simulation stopped!\nAll processes were removed from the queues.\n");
    }

    @Override
    public void displayProcessQueues() {
        String processQueues;
        var readyQueue = "- Ready: ";
        var blockedQueue = "- Blocked: ";
        var finishedQueue = "- Finished: ";

        var readyNames = schedulingStrategy.ready.stream().map(p -> p.getName()).collect(Collectors.toList());
        readyQueue += String.join(", ", readyNames);
        var blockedNames = schedulingStrategy.blocked.stream().map(p -> p.getName()).collect(Collectors.toList());
        blockedQueue += String.join(", ", blockedNames);
        var finishedNames = schedulingStrategy.finished.stream().map(p -> p.getName()).collect(Collectors.toList());
        finishedQueue += String.join(", ", finishedNames);

        processQueues = String.join("\n", readyQueue, blockedQueue, finishedQueue);
        notificationInterface.display("Short Term scheduler:\nProcess queues:\n" + processQueues + "\n");
    }

    public void run() {
        while (true) {

            //This is important to avoid the CPU to be 100% used DONT REMOVE IT
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            //Check if simulation is running
            if (!isRunning) {
                continue;
            }

            if (getProcessLoad() > 0) {
                schedulingStrategy.execute();
                displayProcessQueues();
            }
        }
    }

    private void restart() {
        schedulingStrategy.ready.clear();
        schedulingStrategy.blocked.clear();
        schedulingStrategy.finished.clear();

        return;
    }

}
