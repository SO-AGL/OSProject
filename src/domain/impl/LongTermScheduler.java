package domain.impl;

import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;

public class LongTermScheduler extends Thread implements SubmissionInterface {
    private InterSchedulerInterface interSchedulerInterface = null;
    private NotificationInterface notificationInterface = null;

    private List<Process> submissionQueue = new ArrayList<Process>();
    private int MAX_SUBMISSION_QUEUE_SIZE = 10000;
    private int MAX_PROCESS_OPEN_SIZE = 0;
    private int VERIFY_INTERVAL_MS = 1000;

    public LongTermScheduler(int maxProcessOpenSize) {
        this.MAX_PROCESS_OPEN_SIZE = maxProcessOpenSize;
    }

    public void setInterSchedulerInterface(InterSchedulerInterface interSchedulerInterface) {
        this.interSchedulerInterface = interSchedulerInterface;
    }

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
    }

    @Override
    public boolean submitJob(String fileName) {
        try {
            var newProcess = new Process(fileName);

            if (submissionQueue.size() >= MAX_SUBMISSION_QUEUE_SIZE) {
                notificationInterface.display("LongTermScheduler:\nFull submission queue.");
                return false;
            }

            submissionQueue.add(newProcess);
            displaySubmissionQueue();
            
            return true;
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                notificationInterface.display("LongTermScheduler:\nFile not found.");
            }
            return false;
        }
    }

    @Override
    public void displaySubmissionQueue() {

        String output = "LongTermScheduler:\nSubmission Queue: ";

        for (var process : submissionQueue) {
            output += process.getName() + " ";
        }

        notificationInterface.display(output);

        return;

    }

    public void run() {
        while (true) {

            if (submissionQueue.size() > 0) {

                if (interSchedulerInterface.getProcessLoad() < MAX_PROCESS_OPEN_SIZE) {
                    var newProcess = submissionQueue.get(0);
                    submissionQueue.remove(0);

                    interSchedulerInterface.addProcess(newProcess);

                    notificationInterface.display(
                            "LongTermScheduler:\nProcess " + newProcess.getName() + " added to the ready queue.");
                }

            }

            try {
                Thread.sleep(VERIFY_INTERVAL_MS);
            } catch (InterruptedException e) {
                notificationInterface.display("LongTermScheduler:\nLong Term Scheduler interrupted.");
            }

        }

    }

}
