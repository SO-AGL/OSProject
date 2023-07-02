package application;

import domain.api.SchedulingStrategy;
import domain.impl.LongTermScheduler;
import domain.impl.ShortTermScheduler;
import domain.impl.UserInterface;

public class SchedulerSimulator {
    private UserInterface userInterface;
    private LongTermScheduler longTermScheduler;
    private ShortTermScheduler shortTermScheduler;

    public SchedulerSimulator(int maxProcessReadySize, int quantumTimeMs, SchedulingStrategy schedulingStrategy) {
        shortTermScheduler = new ShortTermScheduler(schedulingStrategy, quantumTimeMs);
        longTermScheduler = new LongTermScheduler(maxProcessReadySize);
        userInterface = new UserInterface(schedulingStrategy.getClass().getSimpleName() + ", max load: " + maxProcessReadySize + ", quantum: " + quantumTimeMs + "ms");

        shortTermScheduler.setNotificationInterface(userInterface);
        longTermScheduler.setInterSchedulerInterface(shortTermScheduler);
        longTermScheduler.setNotificationInterface(userInterface);
        userInterface.setControlInterface(shortTermScheduler);
        userInterface.setSubmissionInterface(longTermScheduler);

        longTermScheduler.start();
        userInterface.start();
    }
}
