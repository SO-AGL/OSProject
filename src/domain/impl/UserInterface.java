package domain.impl;

import domain.api.ControlInterface;
import domain.api.NotificationInterface;
import domain.api.SubmissionInterface;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserInterface implements NotificationInterface {
    private ControlInterface controlInterface;
    private SubmissionInterface submissionInterface;

    private JTextArea infoDisplayText;

    public UserInterface() {
        createFrame();
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
