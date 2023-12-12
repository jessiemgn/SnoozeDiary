package persistence;

import model.Sleep;
import model.SleepTracker;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            SleepTracker st = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptySleepTracker() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySleepTracker.json");
        try {
            SleepTracker st = reader.read();
            assertEquals(0, st.getSleepSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralSleepTracker() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSleepTracker.json");
        try {
            SleepTracker st = reader.read();
            List<Sleep> sleeps = st.getSleepCycles();
            assertEquals(2, sleeps.size());
            checkSleep(sleeps.get(0),  19,15, 4, "Lorem");
            checkSleep(sleeps.get(1),  23,7, 3, "Ipsum");
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}