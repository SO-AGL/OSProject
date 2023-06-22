package domain.impl;

import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;

public class LongTermScheduler extends Thread implements SubmissionInterface {
    private InterSchedulerInterface interSchedulerInterface = null;
    private NotificationInterface notificationInterface = null;

    private List<Process> submissionQueue = new ArrayList<Process>();
    private int MAX_SUBMISSION_QUEUE_SIZE = 10000;
    private int MAX_PROCESS_OPEN_SIZE = 0;
    private int VERIFY_INTERVAL_MS = 100;

    public LongTermScheduler(int maxProcessOpenSize) {
        MAX_PROCESS_OPEN_SIZE = maxProcessOpenSize;
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
            if (submissionQueue.size() >= MAX_SUBMISSION_QUEUE_SIZE) {
                notificationInterface.display("LongTermScheduler: Error! Full submission queue.\n");
                return false;
            }

            var newProcess = new Process(fileName);

            submissionQueue.add(newProcess);
            displaySubmissionQueue();
            
            return true;
        } catch (IOException | IllegalArgumentException e) {
            if (e instanceof NoSuchFileException) {
                notificationInterface.display("LongTermScheduler: Error! File not found.\n");
            } else if (e instanceof IllegalArgumentException) {
                notificationInterface.display("LongTermScheduler: Error! " + e.getMessage());
            } else {
                e.printStackTrace();
                notificationInterface.display("LongTermScheduler: Error! See terminal for more details.");
            }
            return false;
        }
    }

    @Override
    public void displaySubmissionQueue() {
        String output = "LongTermScheduler:\nSubmission Queue: ";
        var processes = submissionQueue.stream().map(p -> p.getName()).collect(Collectors.toList());

        output += String.join(", ", processes);
        notificationInterface.display(output + "\n");

        return;
    }

    public void run() {
        while (true) {

            if (submissionQueue.size() > 0) {

                if (interSchedulerInterface.getProcessLoad() < MAX_PROCESS_OPEN_SIZE) {
                    var newProcess = submissionQueue.remove(0);

                    interSchedulerInterface.addProcess(newProcess);

                    notificationInterface.display(
                            "LongTermScheduler:\nProcess " + newProcess.getName() + " added to the ready queue.\n");
                }

            }

            try {
                Thread.sleep(VERIFY_INTERVAL_MS);
            } catch (InterruptedException e) {
                notificationInterface.display("LongTermScheduler:\nLong Term Scheduler interrupted.\n");
            }

        }

    }

}
