package jpcsp.scheduler;

import org.junit.Assert;
import org.junit.Test;

public class SchedulerActionTest {
    @Test
    public void testCompareTo() {
        SchedulerAction a = new SchedulerAction(100, null);
        SchedulerAction b = new SchedulerAction(200, null);
        SchedulerAction c = new SchedulerAction(100, null);

        Assert.assertTrue("Earlier schedule should be less than later", a.compareTo(b) < 0);
        Assert.assertTrue("Later schedule should be greater than earlier", b.compareTo(a) > 0);
        Assert.assertEquals("Equal schedules should compare as zero", 0, a.compareTo(c));
    }

    @Test
    public void testCompareToWithZeroSchedule() {
        SchedulerAction immediate = new SchedulerAction(0, null);
        SchedulerAction delayed = new SchedulerAction(1000, null);

        Assert.assertTrue("Immediate action should come before delayed", immediate.compareTo(delayed) < 0);
    }
}
