package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import domain.impl.LongTermScheduler;
import domain.impl.ShortTermScheduler;
import domain.impl.ShortestJobFirst;
import domain.impl.UserInterface;
import domain.impl.Process;

public class SchedulerSimulator extends Thread {
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
