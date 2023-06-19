package domain.impl;

import java.util.Scanner;

import domain.api.ControlInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserInterface extends Thread implements NotificationInterface {
    private ControlInterface controlInterface;
    private SubmissionInterface submissionInterface;

    private JTextArea infoDisplayText;

    public UserInterface() {
        createFrame(); // Creates Notifiication window to display thread notifications
    }

    public void setControlInterface(ControlInterface controlInterface) {
        this.controlInterface = controlInterface;
    }

    public void setSubmissionInterface(SubmissionInterface submissionInterface) {
        this.submissionInterface = submissionInterface;
    }

    /**
     * Method used by the schedulers to notify any information related to the simulation.
     * The message is displayed like "INFO: [info]" through the opened notifications JFrame.
     *
     * @param info The information string to be displayed.
     */
    @Override
    public void display(String info) {
        infoDisplayText.append("INFO: " + info + "\n"); // Appends new text info in the display component
        infoDisplayText.setCaretPosition(infoDisplayText.getDocument().getLength()); // Scroll to bottom
    }

    public void run() {
        int option; // User selected option
        var scan = new Scanner(System.in); // Scanner object for the user input
        String fileName; // User selected file to submit

        displayWelcomeMessage();
        do {
            try {
                displayMenu();
                option = Integer.parseInt(scan.nextLine()); // Getting the user option
                switch (option) {
                    case 1:
                        System.out.println("Starting simulation");
                        controlInterface.startSimulation();
                        break;
                    case 2:
                        System.out.println("Suspending simulation");
                        controlInterface.suspendSimulation();
                        break;
                    case 3:
                        System.out.println("Resuming simulation");
                        controlInterface.resumeSimulation();
                        break;
                    case 4:
                        System.out.println("Stopping simulation");
                        controlInterface.stopSimulation();
                        break;
                    case 5:
                        System.out.println("Processes queues displayed in notifications window");
                        controlInterface.displayProcessQueues();
                        break;
                    case 6:
                        System.out.print("Type the name of the file: ");
                        fileName = scan.nextLine();
                        submissionInterface.submitJob(fileName);
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (Exception e) {
                option = 0;
                System.err.println(e);
            }
        } while (option != 4);

        scan.close();
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to the SchedulerSimulator!");
    }

    private void displayMenu() {
        System.out.println("================================");
        System.out.println("Select one of the options below:");
        System.out.println("- Control options:");
        System.out.println("\t(1) Start simulation");
        System.out.println("\t(2) Suspend simulation");
        System.out.println("\t(3) Resume simulation");
        System.out.println("\t(4) Stop simulation");
        System.out.println("\t(5) Display processes queues");
        System.out.println("- Submission options:");
        System.out.println("\t(6) Submit job");
        System.out.print("Option: ");
    }

    private void createFrame() {
        var infoDisplayFrame = new JFrame("Simulation Notifcations");
        infoDisplayText = new JTextArea(10, 30);
        var scrollPane = new JScrollPane(infoDisplayText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        infoDisplayFrame.getContentPane().add(scrollPane);
        infoDisplayFrame.setSize(700, 900);
        infoDisplayFrame.setVisible(true);
    }

}
