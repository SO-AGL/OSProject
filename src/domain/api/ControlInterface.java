package domain.api;

public interface ControlInterface {
    public void startSimulation();

    public void suspendSimulation();

    public void resumeSimulation();

    public void stopSimulation();

    public void displayProcessQueues();
}
