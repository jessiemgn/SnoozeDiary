package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkSleep(Sleep sleep, int sleepTime, int awakeTime, int sleepQuality, String journal) {
        assertEquals(sleep.getSleepTime(), sleepTime);
        assertEquals(sleep.getAwakeTime(), awakeTime);
        assertEquals(sleep.getSleepQuality(), sleepQuality);
        assertEquals(sleep.getJournal(), journal);
    }
}
