package domain.impl;

import java.util.Scanner;

import domain.api.ControlInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

import javax.swing.*;
import java.awt.*;

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

        var frame = new JFrame();
        var panel = new JPanel();

        var panel_output = new JPanel();
        var panel_menu = new JPanel();
        var panel_input = new JPanel();
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var scrollPane_output = new JScrollPane();
        var scrollPane_menu = new JScrollPane();

        var textArea_output = new JTextArea(10, 30);
        var textArea_menu = new JTextArea(10, 30);
        var textArea_input = new JTextField(30);

        var label_output = new JLabel("Simulation output", SwingConstants.CENTER);
        var label_menu = new JLabel("Menu", SwingConstants.CENTER);
        var label_input = new JLabel("Input", SwingConstants.CENTER);
        var label_input_instruct = new JLabel("Type your command and hit ENTER.", SwingConstants.CENTER);

        label_output.setPreferredSize(new Dimension(120, 20));
        label_menu.setPreferredSize(new Dimension(120, 20));
        label_input.setPreferredSize(new Dimension(120, 20));

        scrollPane_output.setViewportView(textArea_output);
        scrollPane_output.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane_menu.setViewportView(textArea_menu);
        scrollPane_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        textArea_output.setEditable(false);
        textArea_menu.setEditable(false);

        panel_output.setLayout(new BoxLayout(panel_output, BoxLayout.X_AXIS));
        panel_menu.setLayout(new BoxLayout(panel_menu, BoxLayout.X_AXIS));
        panel_input.setLayout(new BoxLayout(panel_input, BoxLayout.X_AXIS));

        panel_output.add(label_output);
        panel_output.add(scrollPane_output);
        panel_menu.add(label_menu);
        panel_menu.add(scrollPane_menu);
        panel_input.add(label_input);
        panel_input.add(textArea_input);
        
        panel_output.setPreferredSize(new Dimension(600, 385));
        panel_menu.setPreferredSize(new Dimension(600, 385));
        panel_input.setPreferredSize(new Dimension(600, 30));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(600, 800));

        panel.add(panel_output);
        panel.add(panel_menu);
        panel.add(panel_input);
        panel.add(label_input_instruct);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

    }

}
