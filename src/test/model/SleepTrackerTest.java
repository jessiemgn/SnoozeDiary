package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for SleepTracker class
class SleepTrackerTest {
    private SleepTracker sleepTracker;
    private Sleep sleep1;
    private Sleep sleep2;
    private ArrayList<Sleep> sleepList;

    @BeforeEach
    void runBefore() {
        sleepTracker = new SleepTracker();

        sleep1 = new Sleep(1,5,5,"Journal1");
        sleepTracker.addSleep(sleep1, true);

        sleep2 = new Sleep(2,8,3,"Journal2");
        sleepTracker.addSleep(sleep2, true);
    }

    @Test
    public void testConstructor() {
        sleepList = new ArrayList<>();
        sleepList.add(sleep1);
        sleepList.add(sleep2);
        assertEquals(sleepList, sleepTracker.getSleepCycles());
        assertEquals(1, sleepTracker.getWeekSize());
    }

    @Test
    public void testConstructor2() {
        sleepList = new ArrayList<>();
        sleepList.add(sleep1);
        sleepList.add(sleep2);
        SleepTracker sleepTracker2 = new SleepTracker(sleepList);
        assertEquals(sleepList, sleepTracker2.getSleepCycles());
        assertEquals(1, sleepTracker2.getWeekSize());
    }

    @Test
    public void testGetSleep1() {
        assertEquals(sleep1, sleepTracker.getSleep(1,1));
    }

    @Test
    public void testGetSleep2() {
        assertEquals(sleep2, sleepTracker.getSleep(1,2));
    }

    @Test
    public void testGetSleepNull() {
        assertNull(sleepTracker.getSleep(2,1));
    }

    @Test
    public void testGetSleepNull2() {
        assertNull(sleepTracker.getSleep(2,2));
    }

    @Test
    public void testAddNewSleep() {
        Sleep sleep = new Sleep(3,5,5,"Journal");
        sleepTracker.addSleep(sleep, true);
        assertTrue(sleep.checkWeekAndDay(1,3));
        assertEquals(1, sleepTracker.getWeekSize());
    }

    @Test
    public void testGetSleeps() {
        List<Sleep> sleeps = sleepTracker.getSleepCycles();
        assertEquals(sleeps.get(0), sleep1);
        assertEquals(sleeps.get(1), sleep2);
    }

    @Test
    public void testRemoveLastSleep() {
        sleepTracker.removeLastSleep();
        assertNull(sleepTracker.getSleep(1,2));
    }

    @Test
    public void testSummary() {
        StringBuilder sum = new StringBuilder();
        sum.append("Sleep Cycle Summary:\n");
        sum.append(String.format("%-7s%-7s%-15s%-15s%-20s%-20s%s\n", "Week", "Day", "Sleep Time",
                "Awake Time", "Sleep Quality", "Sleep Duration", "Journal"));

        sum.append(String.format("%-7s%-7s%-15s%-15s%-20s%-20s%s\n",
                sleep1.getWeek(),
                sleep1.getDay(),
                sleep1.getSleepTime(),
                sleep1.getAwakeTime(),
                sleep1.getSleepQuality(),
                sleep1.getDuration(),
                sleep1.getJournal()
        ));

        sum.append(String.format("%-7s%-7s%-15s%-15s%-20s%-20s%s\n",
                sleep2.getWeek(),
                sleep2.getDay(),
                sleep2.getSleepTime(),
                sleep2.getAwakeTime(),
                sleep2.getSleepQuality(),
                sleep2.getDuration(),
                sleep2.getJournal()
        ));

        assertEquals(sum.toString(), sleepTracker.summary());
    }
}