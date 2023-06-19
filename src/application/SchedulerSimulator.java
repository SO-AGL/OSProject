package application;

import domain.impl.LongTermScheduler;
import domain.impl.ShortTermScheduler;
import domain.impl.ShortestJobFirst;
import domain.impl.UserInterface;

public class SchedulerSimulator {
    private UserInterface userInterface;
    private LongTermScheduler longTermScheduler;
    private ShortTermScheduler shortTermScheduler;

    public SchedulerSimulator() {
        var schedulingStrategy = new ShortestJobFirst();
        var quantumTimeMs = 200;

        shortTermScheduler = new ShortTermScheduler(schedulingStrategy, quantumTimeMs);
        longTermScheduler = new LongTermScheduler();
        userInterface = new UserInterface();

        shortTermScheduler.setNotificationInterface(userInterface);
        longTermScheduler.setInterSchedulerInterface(shortTermScheduler);
        longTermScheduler.setNotificationInterface(userInterface);
        userInterface.setControlInterface(shortTermScheduler);
        userInterface.setSubmissionInterface(longTermScheduler);
    }

    public void start() {
        userInterface.start();
    }
}
