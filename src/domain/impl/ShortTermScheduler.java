package domain.impl;

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

        if(!this.isAlive()) {this.start();}

        notificationInterface.display("Simulation started!");
    }

    @Override
    public void suspendSimulation() {
        isRunning = false;

        notificationInterface.display("Simulation suspended!");
    }

    @Override
    public void resumeSimulation() {
        isRunning = true;

        notificationInterface.display("Simulation resumed!");
    }

    @Override
    public void stopSimulation() {
        isRunning = false;
        restart();

        notificationInterface.display("Simulation stopped!\nAll processes were removed from the queues.");
    }

    @Override
    public void displayProcessQueues() {
        String processQueues;
        var readyQueue = "ready: ";
        var blockedQueue = "blocked: ";
        var finishedQueue = "finished: ";

        for (var process : schedulingStrategy.ready) {
            readyQueue += process.getName() + ", ";
        }
        for (var process : schedulingStrategy.blocked) {
            blockedQueue += process.getName() + ", ";
        }
        for (var process : schedulingStrategy.finished) {
            finishedQueue += process.getName() + ", ";
        }

        processQueues = String.join("\n", readyQueue, blockedQueue, finishedQueue);
        notificationInterface.display("Process queues:\n" + processQueues);
    }

    public void run() {

        while (true) {

            //Check if simulation is running
            if (!isRunning) {
            
                //This is important to avoid the CPU to be 100% used DONT REMOVE IT
                try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

                continue;
            }

            schedulingStrategy.execute();
            displayProcessQueues();
        }

    }

    private void restart() {
        schedulingStrategy.ready.clear();
        schedulingStrategy.blocked.clear();
        schedulingStrategy.finished.clear();

        return;
    }

}
