//package ui;
//
//import model.Sleep;
//import model.SleepTracker;
//import persistence.JsonReader;
//import persistence.JsonWriter;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Scanner;
//
//// Sleep Tracker Console
//public class SleepTrackerConsole {
//    private SleepTracker sleepTracker;
//    private final Scanner scanner;
//    private JsonWriter jsonWriter;
//    private JsonReader jsonReader;
//    private static final String JSON_STORE = "./data/sleep-tracker.json";
//
//    // EFFECTS: constructs sleep tracker and runs application
//    public SleepTrackerConsole() throws FileNotFoundException {
//        scanner = new Scanner(System.in);
//        sleepTracker = new SleepTracker();
//        jsonWriter = new JsonWriter(JSON_STORE);
//        jsonReader = new JsonReader(JSON_STORE);
//        welcome();
//    }
//
//    // EFFECTS: greets user and gives the option to load data from file
//    private void welcome() {
//        System.out.println("Welcome to SnoozeDiary");
//        System.out.println("Let's track your sleep cycle!");
//        while (true) {
//            System.out.println("Would you like to load data from file? ('yes' or 'no')");
//            String answer = scanner.nextLine();
//            switch (answer) {
//                case "yes":
//                    loadSleepTracker();
//                    runTracker();
//                    return;
//                case "no":
//                    runTracker();
//                    return;
//            }
//            System.out.println("Answer must be 'yes' or 'no'");
//        }
//    }
//
//    // EFFECTS: prompts user to select between the given options
//    private void runTracker() {
//        while (true) {
//            options();
//            String menu = scanner.nextLine();
//            switch (menu) {
//                case "1":
//                    addSleepCycle();
//                    break;
//                case "2":
//                    case2();
//                    break;
//                case "3":
//                    sleepTracker.removeLastSleep();
//                    System.out.println(sleepTracker.summary());
//                    break;
//                case "4":
//                    System.out.println(sleepTracker.summary());
//                    break;
//                case "0":
//                    exit();
//                    return;
//            }
//        }
//    }
//
//    // EFFECTS: prints out menu options
//    private void options() {
//        System.out.print(
//                "1. Add Sleep\n"
//                        + "2. Modify Sleep Details\n"
//                        + "3. Remove Last Added Sleep\n"
//                        + "4. Sleep Summary\n"
//                        + "0. Quit\n"
//                        + "Choose: "
//        );
//    }
//
//    // EFFECTS: modifies sleep if sleep exists
//    private void case2() {
//        while (true) {
//            Sleep sleep = getSleep();
//            if (sleep == null) {
//                sleepNotExist();
//                return;
//            }
//            modifySleep(sleep);
//            return;
//        }
//    }
//
//    // EFFECTS: gives the option to save changes to file
//    private void exit() {
//        while (true) {
//            System.out.println("Would you like to save changes to file? ('yes' or 'no')");
//            String ans = scanner.nextLine();
//            switch (ans) {
//                case "yes":
//                    saveSleepTracker();
//                    return;
//                case "no":
//                    System.out.println("Thank you for using SnoozeDiary!");
//                    return;
//            }
//            System.out.println("Answer must be 'yes' or 'no'");
//        }
//    }
//
//    // EFFECTS: return sleep for a certain week and day
//    private Sleep getSleep() {
//        System.out.print("Week : ");
//        int week = Integer.parseInt(scanner.nextLine());
//        System.out.print("Day : ");
//        int day = Integer.parseInt(scanner.nextLine());
//        return sleepTracker.getSleep(week, day);
//    }
//
//    // MODIFIES: sleepTracker
//    // EFFECTS: adds new sleep
//    public void addSleepCycle() {
//        int sleepTime = addSleepDetailsHour("Sleep Time (0-23): ");
//        int awakeTime = addSleepDetailsHour("Awake Time (0-23): ");
//        int sleepQuality = addSleepDetailsInteger("Sleep Quality (1-5): ");
//        String journal = addSleepDetailsString("Journal: ");
//        sleepTracker.addSleep(new Sleep(sleepTime, awakeTime, sleepQuality, journal));
//        System.out.println(sleepTracker.summary());
//    }
//
//    // EFFECTS: adds sleep details for hour value
//    public int addSleepDetailsHour(String element) {
//        do {
//            System.out.print(element);
//            int hour = Integer.parseInt(scanner.nextLine());
//            if (0 <= hour && hour <= 23) {
//                return hour;
//            }
//            System.out.println("Answer has to be 0-23");
//        } while (true);
//    }
//
//    // EFFECTS: adds sleep details for integer value
//    public int addSleepDetailsInteger(String element) {
//        do {
//            System.out.print(element);
//            int q = Integer.parseInt(scanner.nextLine());
//            if (1 <= q && q <= 5) {
//                return q;
//            }
//            System.out.println("Answer has to be 1-5");
//        } while (true);
//    }
//
//    // EFFECTS: adds sleep details for string value
//    public String addSleepDetailsString(String element) {
//        System.out.print(element);
//        return scanner.nextLine();
//    }
//
//    //EFFECTS: notify user if sleep for given week and day is not in tracker
//    public void sleepNotExist() {
//        System.out.println("There is no sleep tracked for this week and day.");
//    }
//
//    //EFFECTS: modify sleep details
//    public void modifySleep(Sleep sleep) {
//        System.out.println(sleep.getWeekAndDay());
//        int sleepTime = addSleepDetailsHour("Sleep Time (0-23): ");
//        int awakeTime = addSleepDetailsHour("Awake Time (0-23): ");
//        int sleepQuality = addSleepDetailsInteger("Sleep Quality (1-5): ");
//        String journal = addSleepDetailsString("Journal: ");
//        sleep.modifySleepDetails(sleepTime, awakeTime, sleepQuality, journal);
//        System.out.println(sleepTracker.summary());
//    }
//
//    // EFFECTS: saves sleep tracker to file
//    private void saveSleepTracker() {
//        try {
//            jsonWriter.open();
//            jsonWriter.write(sleepTracker);
//            jsonWriter.close();
//            System.out.println("Saved sleep tracker data to: " + JSON_STORE);
//            System.out.println("Thank you for using SnoozeDiary!");
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write data to file: " + JSON_STORE);
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: loads sleep tracker from file
//    private void loadSleepTracker() {
//        try {
//            sleepTracker = jsonReader.read();
//            System.out.println("Loaded data from: " + JSON_STORE);
//        } catch (IOException e) {
//            System.out.println("Unable to read data from file: " + JSON_STORE);
//        }
//    }
//}