package application;

import domain.impl.LongTermScheduler;
import domain.impl.SchedulingStrategy;
import domain.impl.ShortTermScheduler;
import domain.impl.UserInterface;

public class SchedulerSimulator {
    private UserInterface userInterface;
    private LongTermScheduler longTermScheduler;
    private ShortTermScheduler shortTermScheduler;

    public SchedulerSimulator(int maxShortTermSchedulerLoad, int quantumSizeMs, SchedulingStrategy schedulingStrategy) {
        shortTermScheduler = new ShortTermScheduler(schedulingStrategy, quantumSizeMs);
        longTermScheduler = new LongTermScheduler(maxShortTermSchedulerLoad);
        userInterface = new UserInterface(schedulingStrategy.getClass().getSimpleName() + ", max load: "
                + maxShortTermSchedulerLoad + ", quantum: " + quantumSizeMs + "ms");

        shortTermScheduler.setNotificationInterface(userInterface);
        longTermScheduler.setInterSchedulerInterface(shortTermScheduler);
        longTermScheduler.setNotificationInterface(userInterface);
        userInterface.setControlInterface(shortTermScheduler);
        userInterface.setSubmissionInterface(longTermScheduler);

        longTermScheduler.start();
        userInterface.start();
    }
}
