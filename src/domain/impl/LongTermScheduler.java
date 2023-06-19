package domain.impl;

import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

import java.util.*;

public class LongTermScheduler extends Thread implements SubmissionInterface {
    private InterSchedulerInterface interSchedulerInterface = null;
    private NotificationInterface notificationInterface = null;

    private List<Process> submissionQueue = new ArrayList<Process>();
    private int MAX_SUBMISSION_QUEUE_SIZE = 10000;
    private int MAX_PROCESS_OPEN_SIZE = 2;
    private int VERIFY_INTERVAL_MS = 1000;

    public void setInterSchedulerInterface(InterSchedulerInterface interSchedulerInterface) {
        this.interSchedulerInterface = interSchedulerInterface;
    }

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
    }

    @Override
    public boolean submitJob(String fileName) {
        var newProcess = new Process("../data/" + fileName);

        if (submissionQueue.size() >= MAX_SUBMISSION_QUEUE_SIZE) {
            return false;
        }

        submissionQueue.add(newProcess);
        displaySubmissionQueue();
        return true;
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
        // TODO Auto-generated method stub

        while(true) {

            if (submissionQueue.size() > 0) {

                if (interSchedulerInterface.getProcessLoad() < MAX_PROCESS_OPEN_SIZE) {
                    var newProcess = submissionQueue.get(0);
                    submissionQueue.remove(0);

                    interSchedulerInterface.addProcess(newProcess);

                    notificationInterface.display("LongTermScheduler:\nProcess " + newProcess.getName() + " added to the ready queue.");
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
