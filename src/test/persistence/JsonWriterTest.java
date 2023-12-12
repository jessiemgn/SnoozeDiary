package persistence;

import model.Sleep;
import model.SleepTracker;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            SleepTracker st = new SleepTracker();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptySleepTracker() {
        try {
            SleepTracker st = new SleepTracker();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySleepTracker.json");
            writer.open();
            writer.write(st);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySleepTracker.json");
            st = reader.read();
            assertEquals(0, st.getSleepSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralSleepTracker() {
        try {
            SleepTracker st = new SleepTracker();

            Sleep sleep1 = new Sleep(19,15, 4, "Lorem");
            st.addSleep(sleep1, true);

            Sleep sleep2 = new Sleep(23,7, 3, "Ipsum");
            st.addSleep(sleep2, true);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSleepTracker.json");
            writer.open();
            writer.write(st);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralSleepTracker.json");
            st = reader.read();
            List<Sleep> sleeps = st.getSleepCycles();
            assertEquals(2, sleeps.size());

            checkSleep(sleeps.get(0),  sleep1.getSleepTime(), sleep1.getAwakeTime(), sleep1.getSleepQuality(), sleep1.getJournal());
            checkSleep(sleeps.get(1),  sleep2.getSleepTime(), sleep2.getAwakeTime(), sleep2.getSleepQuality(), sleep2.getJournal());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}