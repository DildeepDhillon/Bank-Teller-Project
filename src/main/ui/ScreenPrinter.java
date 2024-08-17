package ui;

import model.Event;
import model.EventLog;

import java.awt.Component;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Represents a screen printer for printing event log to screen.
 */
public class ScreenPrinter extends JInternalFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JTextArea logArea;


    // REQUIRES: parent != null
    // MODIFIES: this
    // EFFECTS: creates a JScrollPane
    public ScreenPrinter(Component parent) {
        super("Event log", false, true, false, false);
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane);
        setSize(WIDTH, HEIGHT);
        setPosition(parent);
        setVisible(true);
    }

    // REQUIRES: el != null
    // MODIFIES: this
    // EFFECTS: for each event in EventLog, adds date and description to the logArea
    public void printLog(EventLog el) {
        for (Event next : el) {
            logArea.setText(logArea.getText() + next.toString() + "\n\n");
        }

        repaint();
    }


    // MODIFIES: this
    // EFFECTS: sets the position of the window in which the log will be printed relative to the parent
    private void setPosition(Component parent) {
        setLocation(parent.getWidth() - getWidth() - 20,
                parent.getHeight() - getHeight() - 20);
    }
}
