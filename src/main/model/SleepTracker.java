package model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents a List of Sleep
public class SleepTracker implements Writable {
    // add fields to represent changing properties of SleepTracker
    private final ArrayList<Sleep> sleeps;

    // EFFECTS: constructs a SleepTracker with empty Sleep
    public SleepTracker() {
        sleeps = new ArrayList<>();
    }

    // EFFECTS: constructs a SleepTracker with sleeps
    public SleepTracker(ArrayList<Sleep> sleeps) {
        this.sleeps = sleeps;
    }

    // EFFECTS: add new sleep
    public void addSleep(Sleep sleep, boolean newSleep) {
        int num = sleeps.size() + 1;
        int week = (num - 1) / 7 + 1;
        int day = (num - 1) % 7 + 1;
        sleep.setWeekAndDay(week, day);
        sleeps.add(sleep);
        if (newSleep) {
            EventLog.getInstance().logEvent(new Event("added new sleep cycle."));
        }
    }

    // REQUIRES: there is at least one sleep
    // EFFECTS: remove last added sleep
    public void removeLastSleep() {
        sleeps.remove(sleeps.size() - 1);
        EventLog.getInstance().logEvent(new Event("removed most recent sleep cycle."));
    }

    // EFFECTS: output the sleep summary for each day
    public String summary() {
        StringBuilder sum = new StringBuilder();
        sum.append("Sleep Cycle Summary:\n");
        sum.append(String.format("%-7s%-7s%-15s%-15s%-20s%-20s%s\n", "Week", "Day", "Sleep Time",
                "Awake Time", "Sleep Quality", "Sleep Duration", "Journal"));
        for (Sleep sleep : sleeps) {
            sum.append(String.format("%-7s%-7s%-15s%-15s%-20s%-20s%s\n",
                    sleep.getWeek(),
                    sleep.getDay(),
                    sleep.getSleepTime(),
                    sleep.getAwakeTime(),
                    sleep.getSleepQuality(),
                    sleep.getDuration(),
                    sleep.getJournal()
            ));
        }
        return sum.toString();
    }

    // EFFECTS: return sleeps
    public ArrayList<Sleep> getSleepCycles() {
        return sleeps;
    }

    // EFFECTS: return sleep for given week and day
    public Sleep getSleep(int week, int day) {
        for (Sleep sleep : sleeps) {
            if (sleep.checkWeekAndDay(week,day)) {
                return sleep;
            }
        }
        return null;
    }

    // EFFECTS: returns size of sleeps
    public int getSleepSize() {
        return sleeps.size();
    }

    // EFFECTS: returns number of week
    public int getWeekSize() {
        int num = sleeps.size();
        return (num / 7) + 1;
    }

    @Override
    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Sleep Tracker", sleepsToJson());
        return json;
    }

    // EFFECTS: returns sleeps in this sleep tracker as a JSON array
    private JSONArray sleepsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Sleep s : sleeps) {
            jsonArray.put(s.toJson());
        }
        return jsonArray;
    }
}
