import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import application.SchedulerSimulator;
import domain.api.SchedulingStrategy;
import domain.impl.RoundRobin;
import domain.impl.ShortestJobFirst;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

public class App {
    static JFrame frame = new JFrame("Process Scheduling Simulator");
    static JPanel mainPanel = new JPanel();
    static JLabel strategyLabel = new JLabel("Scheduling strategy:", SwingConstants.CENTER);
    static JComboBox<String> strategyComboBox = new JComboBox<>(new String[] { "Shortest Job First", "Round Robin" });
    static JLabel loadLabel = new JLabel("Max ShortTermScheduler load (int >0):", SwingConstants.CENTER);
    static JFormattedTextField loadField = new JFormattedTextField(new NumberFormatter());
    static JLabel quantumLabel = new JLabel("Quantum size in milliseconds (int >0):", SwingConstants.CENTER);
    static JFormattedTextField quantumField = new JFormattedTextField(new NumberFormatter());
    static JPanel buttonsPanel = new JPanel();
    static JButton startButton = new JButton("Start");
    static JButton exitButton = new JButton("Exit");

    public static void main(String[] args) throws Exception {
        var argsList = Arrays.asList(args); // convert arguments array to list to facilitate finding the indexes of the
                                            // flags
        int npIndex = argsList.indexOf("-np"); // index of the number of process flag in args array
        int quantumIndex = argsList.indexOf("-qt"); // index of the number of process flag in args array
        int stratIndex = argsList.indexOf("-strat"); // index of the strategy flag in args array (rr or sjf)
        int maxProcessReadySize = 100; // default max number of processes in the ready queue
        int quantumTimeMs = 200;
        String stratName = "sjf"; // default strategy: SJF
        SchedulingStrategy strategy;

        if (npIndex != -1) { // the -np flag was used
            try {
                maxProcessReadySize = Integer.parseInt(args[npIndex + 1]);
                if (maxProcessReadySize <= 0) {
                    throw new NumberFormatException();
                }
            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException) { // if the user uses the flag and don't inform a value
                                                                   // in sequence
                    throw new ArrayIndexOutOfBoundsException("If using -np flag, inform a value (>0)");
                }
                if (e instanceof NumberFormatException) { // if the value informed wasn't valid (couldn't convert to
                                                          // int, or <=0)
                    throw new NumberFormatException("Inform a valid value for np (>0)");
                }
                throw e;
            }
        }

        if (quantumIndex != -1) {
            try {
                quantumTimeMs = Integer.parseInt(args[quantumIndex + 1]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Inform a valid value for qt (>0)");
            }
        }

        if (stratIndex != -1) { // the -strat flag was used
            try {
                stratName = args[stratIndex + 1];
                if (!stratName.equals("sjf") && !stratName.equals("rr")) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException) { // if the user uses the flag and don't inform a value
                                                                   // in sequence
                    throw new ArrayIndexOutOfBoundsException("If using -strat flag, inform a value ('rr' or 'sjf')");
                }
                if (e instanceof IllegalArgumentException) { // if the value informed wasn't valid (not 'sjf' nor 'rr')
                    throw new IllegalArgumentException("Inform a valid strategy ('rr' or 'sjf')");
                }
                throw e;
            }
        }

        if (stratName.equals("rr")) {
            strategy = new RoundRobin();
        } else {
            strategy = new ShortestJobFirst();
        }

        createGUI();

        if (npIndex != -1 || stratIndex != -1 || quantumIndex != -1) {
            new SchedulerSimulator(maxProcessReadySize, quantumTimeMs, strategy);
        }

    }

    static void createGUI() {
        startButton.addActionListener(e -> {
            SchedulingStrategy strategy = null;
            int maxLoad = 0;
            int quantumSizeMs = 0;

            if (strategyComboBox.getSelectedItem().equals("Shortest Job First")) {
                strategy = new ShortestJobFirst();
            } else if (strategyComboBox.getSelectedItem().equals("Round Robin")) {
                strategy = new RoundRobin();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid strategy", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                maxLoad = Integer.parseInt(loadField.getText());
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(frame, "Invalid load", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                quantumSizeMs = Integer.parseInt(quantumField.getText());
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(frame, "Invalid quantum size", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (strategy != null) {
                new SchedulerSimulator(maxLoad, quantumSizeMs, strategy);
            }
        });

        exitButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        loadField.setValue(10);
        quantumField.setValue(200);

        mainPanel.add(strategyLabel);
        mainPanel.add(strategyComboBox);
        mainPanel.add(quantumLabel);
        mainPanel.add(quantumField);
        mainPanel.add(loadLabel);
        mainPanel.add(loadField);
        mainPanel.add(buttonsPanel);
        buttonsPanel.add(startButton);
        buttonsPanel.add(exitButton);

        loadField.setColumns(5);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
