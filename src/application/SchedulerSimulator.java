package application;

import domain.impl.LongTermScheduler;
import domain.impl.ShortTermScheduler;
import domain.impl.UserInterface;

public class SchedulerSimulator {
    private UserInterface userInterface;
    private LongTermScheduler longTermScheduler;
    private ShortTermScheduler shortTermScheduler;

    public SchedulerSimulator() {
        shortTermScheduler = new ShortTermScheduler();
        longTermScheduler = new LongTermScheduler();
        userInterface = new UserInterface();

        shortTermScheduler.setNotificationInterface(userInterface);
        longTermScheduler.setInterSchedulerInterface(shortTermScheduler);
        longTermScheduler.setNotificationInterface(userInterface);
        userInterface.setControlInterface(shortTermScheduler);
        userInterface.setSubmissionInterface(longTermScheduler);
    }
}
