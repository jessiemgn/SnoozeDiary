package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Unit tests for Sleep class
class SleepTest {
    private Sleep sleep;
    private Sleep sleep2;

    @BeforeEach
    void runBefore() {
        sleep = new Sleep(1,3,1,"TEST");
        sleep2 = new Sleep(3,1,1,"TEST");
    }

    @Test
    public void testConstructor() {
        assertEquals(1, sleep.getSleepTime());
        assertEquals(3, sleep.getAwakeTime());
        assertEquals(1, sleep.getSleepQuality());
        assertEquals("TEST", sleep.getJournal());
        assertEquals(2, sleep.getDuration());
        assertEquals("Week 0 Day 0", sleep.getWeekAndDay());
    }

    @Test
    public void testConstructor2() {
        assertEquals(3, sleep2.getSleepTime());
        assertEquals(1, sleep2.getAwakeTime());
        assertEquals(1, sleep2.getSleepQuality());
        assertEquals("TEST", sleep2.getJournal());
        assertEquals(22, sleep2.getDuration());
        assertEquals("Week 0 Day 0", sleep2.getWeekAndDay());
    }

    @Test
    public void testSetSleepTime() {
        sleep.setSleepTime(2);
        assertEquals(2, sleep.getSleepTime());
    }

    @Test
    public void testSetAwakeTime() {
        sleep.setAwakeTime(5);
        assertEquals(5, sleep.getAwakeTime());
    }

    @Test
    public void testSetJournal() {
        sleep.setJournal("TEST2");
        assertEquals("TEST2", sleep.getJournal());
    }

    @Test
    public void testSetSleepQuality() {
        sleep.setSleepQuality(5);
        assertEquals(5, sleep.getSleepQuality());
    }

    @Test
    public void testWeekAndDay() {
        sleep.setWeekAndDay(2,3);
        assertEquals(2, sleep.getWeek());
        assertEquals(3, sleep.getDay());
        assertTrue(sleep.checkWeekAndDay(2,3));
    }

    @Test
    public void testModify() {
        sleep.modifySleepDetails(3,1,5,"Journal 1");
        assertEquals(3, sleep.getSleepTime());
        assertEquals(1, sleep.getAwakeTime());
        assertEquals(5, sleep.getSleepQuality());
        assertEquals("Journal 1", sleep.getJournal());
        assertEquals(22, sleep.getDuration());
    }

    @Test
    public void testModify2() {
        sleep.modifySleepDetails(1,5,5,"Journal 1");
        assertEquals(1, sleep.getSleepTime());
        assertEquals(5, sleep.getAwakeTime());
        assertEquals(5, sleep.getSleepQuality());
        assertEquals("Journal 1", sleep.getJournal());
        assertEquals(4, sleep.getDuration());
    }

    @Test
    public void testModify3() {
        sleep.modifySleepDetails(1,8,5,"Journal 1");
        assertEquals(1, sleep.getSleepTime());
        assertEquals(8, sleep.getAwakeTime());
        assertEquals(5, sleep.getSleepQuality());
        assertEquals("Journal 1", sleep.getJournal());
        assertEquals(7, sleep.getDuration());
    }

    @Test
    public void testModify4() {
        sleep.modifySleepDetails(1,10,5,"Journal 1");
        assertEquals(1, sleep.getSleepTime());
        assertEquals(10, sleep.getAwakeTime());
        assertEquals(5, sleep.getSleepQuality());
        assertEquals("Journal 1", sleep.getJournal());
        assertEquals(9, sleep.getDuration());
    }
}