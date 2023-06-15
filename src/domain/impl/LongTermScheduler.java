package domain.impl;

import domain.api.InterSchedulerInterface;
import domain.api.SubmissionInterface;

public class LongTermScheduler extends Thread implements SubmissionInterface {
    private InterSchedulerInterface interSchedulerInterface = null;

    public void setInterSchedulerInterface(InterSchedulerInterface interSchedulerInterface) {
        this.interSchedulerInterface = interSchedulerInterface;
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
