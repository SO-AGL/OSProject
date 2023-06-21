package domain.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import domain.api.ControlInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

public class UserInterface extends Thread implements NotificationInterface {
    
    private ControlInterface controlInterface;
    private SubmissionInterface submissionInterface;

    private JFrame frame;
    private JTextArea textArea_output;
    private JTextArea textArea_menu;
    private JTextField textArea_input;

    private boolean enterClick = false;

    public UserInterface() {
        createFrame(); // Creates Notification window to display thread notifications
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
        textArea_output.append("INFO: " + info + "\n\n"); // Appends new text info in the display component
        textArea_output.setCaretPosition(textArea_output.getDocument().getLength()); // Scroll to bottom
    }

    public void displayInMenu(String menu) {
        textArea_menu.append(menu + "\n"); // Appends new text info in the display component
        textArea_menu.setCaretPosition(textArea_menu.getDocument().getLength()); // Scroll to bottom
    }

    public String getInput() {
        enterClick = false;

        while(!enterClick) {
            try {
                Thread.sleep(10);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String input = textArea_input.getText();

        // get input again if the user hits enter with an empty input
        if (input.equals("")) {
            return getInput();
        }

        textArea_input.setText("");

        displayInMenu(input);

        return input;
    }

    public void run() {
        int option = 0; // User selected option
        String fileName = ""; // User selected file to submit

        displayWelcomeMessage();

        do {

            try {

                showMenu();
                option = Integer.parseInt(getInput());

                switch (option) {
                    case 1:
                        displayInMenu("Starting simulation");
                        controlInterface.startSimulation();
                        break;
                    case 2:
                        displayInMenu("Suspending simulation");
                        controlInterface.suspendSimulation();
                        break;
                    case 3:
                        displayInMenu("Resuming simulation");
                        controlInterface.resumeSimulation();
                        break;
                    case 4:
                        displayInMenu("Stopping simulation");
                        controlInterface.stopSimulation();
                        break;
                    case 5:
                        displayInMenu("Processes queues displayed in notifications window");
                        controlInterface.displayProcessQueues();
                        break;
                    case 6:
                        displayInMenu("Exiting");
                        break;
                    case 7:
                        displayInMenu("Type the name of the file: ");
                        fileName = getInput();
                        submissionInterface.submitJob(fileName);
                        break;
                    default:
                        displayInMenu("Invalid option");
                        break;
                }

            } catch (Exception e) {
                option = 0;

                displayInMenu("Error!");
                System.err.println(e);
            }

        } while (option != 6);

        displayInMenu("\nHalting... Bye bye!");
        System.out.println("\nHalting... Bye bye!");

        try{
            Thread.sleep(2000);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        frame.dispose();
        System.exit(0);
        
    }

    private void displayWelcomeMessage() {
        displayInMenu("Welcome to the Scheduler Simulator!");
        System.out.println("Welcome to the Scheduler Simulator!");
    }

    private void showMenu() {

        String menuString = ""
        .concat("\n==============================================")
        .concat("\nSelect one of the options below:")
        .concat("\n\n- Control options:")
        .concat("\n\t(1) Start simulation")
        .concat("\n\t(2) Suspend simulation")
        .concat("\n\t(3) Resume simulation")
        .concat("\n\t(4) Stop simulation")
        .concat("\n\t(5) Display processes queues")
        .concat("\n\t(6) Exit")
        .concat("\n\n- Submission options")
        .concat("\n\t(7) Submit job")
        .concat("\n\n==============================================");

        displayInMenu(menuString);

    }

    private void createFrame() {

        frame = new JFrame("Scheduler Simulator");
        var panel = new JPanel();

        var panel_output = new JPanel();
        var panel_menu = new JPanel();
        var panel_input = new JPanel();
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var scrollPane_output = new JScrollPane();
        var scrollPane_menu = new JScrollPane();

        textArea_output = new JTextArea(10, 30);
        textArea_menu = new JTextArea(10, 30);
        textArea_input = new JTextField(30);

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

        textArea_input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterClick = true;
            }
        });

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

    }

}
