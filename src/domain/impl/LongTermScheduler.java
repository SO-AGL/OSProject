package domain.impl;

import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

public class LongTermScheduler extends Thread implements SubmissionInterface {
    private InterSchedulerInterface interSchedulerInterface = null;
    private NotificationInterface notificationInterface = null;

    public void setInterSchedulerInterface(InterSchedulerInterface interSchedulerInterface) {
        this.interSchedulerInterface = interSchedulerInterface;
    }

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
    }

    @Override
    public boolean submitJob(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'submitJob'");
    }

    @Override
    public void displaySubmissionQueue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displaySubmissionQueue'");
    }

    public void run() {
        // TODO Auto-generated method stub
        
    }

}
