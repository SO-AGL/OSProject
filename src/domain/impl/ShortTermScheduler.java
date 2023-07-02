package domain.impl;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import domain.api.ControlInterface;
import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;
import domain.api.SchedulingStrategy;

public class ShortTermScheduler extends Thread implements ControlInterface, InterSchedulerInterface {
    private NotificationInterface notificationInterface;
    private SchedulingStrategy schedulingStrategy;

    private boolean isRunning = false;

    // This is important to avoid ConcurrentModificationException, because
    // while the queue is being printed, the LongTermScheduler can add a
    // process to the ready queue
    private Semaphore readySemaphore = new Semaphore(1);

    public ShortTermScheduler(SchedulingStrategy schedulingStrategy, int quantumSizeMs) {
        this.schedulingStrategy = schedulingStrategy;
        this.schedulingStrategy.setQuantumSizeMs(quantumSizeMs);
    }

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
        this.schedulingStrategy.setNotificationInterface(notificationInterface);
    }

    @Override
    public void addProcess(Process process) {
        try {
            readySemaphore.acquire();
            schedulingStrategy.ready.add(process);
            readySemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getProcessLoad() {
        return schedulingStrategy.ready.size() + schedulingStrategy.blocked.size()
                + (schedulingStrategy.isExecutingProcess() ? 1 : 0);
    }

    @Override
    public void startSimulation() {
        isRunning = true;

        if (!isAlive()) {
            start();
        }

        notificationInterface.display("ShortTermScheduler: \nSimulation started!");
    }

    @Override
    public void suspendSimulation() {
        isRunning = false;

        notificationInterface.display("ShortTermScheduler: \nSimulation suspended!");
    }

    @Override
    public void resumeSimulation() {
        isRunning = true;

        notificationInterface.display("ShortTermScheduler: \nSimulation resumed!");
    }

    @Override
    public void stopSimulation() {
        isRunning = false;
        restart();

        notificationInterface
                .display("ShortTermScheduler: \nSimulation stopped! \nAll processes were removed from the queues.");
    }

    @Override
    public void displayProcessQueues() {
        String processQueues;
        var readyQueue = "\n> Ready:\t";
        var blockedQueue = "\n> Blocked:\t";
        var finishedQueue = "\n> Finished:\t";
        List<String> readyNames;

        try {
            readySemaphore.acquire();
            readyNames = schedulingStrategy.ready.stream().map(p -> p.getName()).collect(Collectors.toList());
            readySemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        readyQueue += String.join(", ", readyNames);
        var blockedNames = schedulingStrategy.blocked.stream().map(p -> p.getName()).collect(Collectors.toList());
        blockedQueue += String.join(", ", blockedNames);
        var finishedNames = schedulingStrategy.finished.stream().map(p -> p.getName()).collect(Collectors.toList());
        finishedQueue += String.join(", ", finishedNames);

        processQueues = String.join("\n", readyQueue, blockedQueue, finishedQueue);
        notificationInterface.display("ShortTermScheduler: \nProcess queues \n" + processQueues);
    }

    public void run() {
        while (true) {

            // This is important to avoid the CPU to be 100% used DONT REMOVE IT
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

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
