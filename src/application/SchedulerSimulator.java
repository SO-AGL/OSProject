package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import domain.impl.LongTermScheduler;
import domain.impl.ShortTermScheduler;
import domain.impl.UserInterface;
import domain.impl.Process;

public class SchedulerSimulator extends Thread {
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

        start();
    }

    @Override
    public void run() {
        var process = new Process("data/teste2.txt");
        shortTermScheduler.addProcess(process);

        try {
            Files.walk(Paths.get("data/processes"))
                .filter(Files::isRegularFile)
                .forEach(file -> shortTermScheduler.addProcess(new Process(file.toString())));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        shortTermScheduler.startSimulation();
    }

    public void start() {
        userInterface.start();
    }
}
