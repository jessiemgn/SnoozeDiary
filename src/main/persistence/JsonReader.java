package persistence;

import model.Sleep;
import model.SleepTracker;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads sleep tracker from file and returns it;
    public SleepTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses sleep tracker from JSON object and returns it
    private SleepTracker parseWorkRoom(JSONObject jsonObject) {
        SleepTracker st = new SleepTracker();
        addSleeps(st, jsonObject);
        return st;
    }

    // MODIFIES: st
    // EFFECTS: parses sleeps from JSON object and adds it to sleep tracker
    private void addSleeps(SleepTracker st, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Sleep Tracker");
        for (Object json : jsonArray) {
            JSONObject sleep = (JSONObject) json;
            addSleep(st, sleep);
        }
    }

    // MODIFIES: st
    // EFFECTS: parses sleep from JSON object and adds it to sleep tracker
    private void addSleep(SleepTracker st, JSONObject jsonObject) {
        int sleepTime = jsonObject.getInt("Sleep Time");
        int awakeTime = jsonObject.getInt("Awake Time");
        int sleepQuality = jsonObject.getInt("Sleep Quality");
        String journal = jsonObject.getString("Journal");
        st.addSleep(new Sleep(sleepTime, awakeTime, sleepQuality, journal), false);
    }
}
