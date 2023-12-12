package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a Sleep
public class Sleep implements Writable {
    // add fields to represent changing properties of Sleep
    private int week;
    private int day;
    private int sleepTime;
    private int awakeTime;
    private int sleepQuality;
    private int duration;
    private String journal;

    // REQUIRES: sleepTime, awakeTime, and sleepQuality must be filled
    // EFFECTS: constructs a Sleep Cycle with given sleepTime and awakeTime,
    //          calculates the sleepDuration
    public Sleep(int sleepTime, int awakeTime, int sleepQuality, String journal) {
        this.sleepTime = sleepTime;
        this.awakeTime = awakeTime;
        this.sleepQuality = sleepQuality;
        if (sleepTime > awakeTime) {
            awakeTime += 24;
        }
        this.duration = awakeTime - sleepTime;
        this.journal = journal;
    }

    // EFFECTS: returns sleep time
    public int getSleepTime() {
        return sleepTime;
    }

    // EFFECTS: returns wake-up time
    public int getAwakeTime() {
        return awakeTime;
    }

    // EFFECTS: returns sleep quality
    public int getSleepQuality() {
        return sleepQuality;
    }

    // EFFECTS: returns duration
    public int getDuration() {
        return duration;
    }

    // EFFECTS: returns journal
    public String getJournal() {
        return journal;
    }

    // MODIFIES: this
    // EFFECTS: sets sleepTime
    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    // REQUIRES: sleepTime must be filled
    // MODIFIES: this
    // EFFECTS: sets awakeTime
    public void setAwakeTime(int awakeTime) {
        this.awakeTime = awakeTime;
    }

    // REQUIRES: sleepTime and awakeTime must be filled
    // MODIFIES: this
    // EFFECTS: sets sleepQuality
    public void setSleepQuality(int sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    // REQUIRES: sleepTime and awakeTime must be filled
    // MODIFIES: this
    // EFFECTS: sets journal
    public void setJournal(String journal) {
        this.journal = journal;
    }

    // REQUIRES: sleep details must be filled
    // EFFECTS: modify sleep details
    public void modifySleepDetails(int sleepTime, int awakeTime, int sleepQuality, String journal) {
        this.sleepTime = sleepTime;
        this.awakeTime = awakeTime;
        this.sleepQuality = sleepQuality;
        if (sleepTime > awakeTime) {
            awakeTime += 24;
        }
        this.duration = awakeTime - sleepTime;
        this.journal = journal;
        EventLog.getInstance().logEvent(new Event("modified sleep details for week "
                + getWeek() + " day " + getDay() + "."));
    }

    // EFFECTS: get the specific week and day for a sleep
    public String getWeekAndDay() {
        return "Week " + week + " Day " + day;
    }

    // EFFECTS: return week of sleep
    public int getWeek() {
        return week;
    }

    // EFFECTS: return day of sleep
    public int getDay() {
        return day;
    }

    public void setWeekAndDay(int week, int day) {
        this.week = week;
        this.day = day;
    }

    // EFFECTS: return true if given week and day has a sleep
    public boolean checkWeekAndDay(int week, int day) {
        return (this.week == week && this.day == day);
    }

    @Override
    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Sleep Time", sleepTime);
        json.put("Awake Time", awakeTime);
        json.put("Sleep Quality", sleepQuality);
        json.put("Journal", journal);
        return json;
    }
}
