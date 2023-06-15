package domain.impl;

import domain.api.ControlInterface;
import domain.api.InterSchedulerInterface;
import domain.api.NotificationInterface;

public class ShortTermScheduler extends Thread implements ControlInterface, InterSchedulerInterface {
    private NotificationInterface notificationInterface;

    public void setNotificationInterface(NotificationInterface notificationInterface) {
        this.notificationInterface = notificationInterface;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
