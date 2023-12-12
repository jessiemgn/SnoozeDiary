package ui;

import model.EventLog;

import java.io.FileNotFoundException;

// Run the SleepTracker
public class Main {
    public static void main(String[] args) {
        try {
            new SleepTrackerGUI();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> EventLog.getInstance().printEventLog()));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}

