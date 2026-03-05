package jpcsp.settings;

import org.junit.Assert;
import org.junit.Test;

public class PerformanceProfileTest {

    @Test
    public void testFromSettingsValueNull() {
        Assert.assertNull(PerformanceProfile.fromSettingsValue(null));
    }

    @Test
    public void testFromSettingsValueEmpty() {
        Assert.assertNull(PerformanceProfile.fromSettingsValue(""));
    }

    @Test
    public void testFromSettingsValueUnknown() {
        Assert.assertNull(PerformanceProfile.fromSettingsValue("UNKNOWN_PROFILE"));
    }

    @Test
    public void testFromSettingsValueLowSpec() {
        Assert.assertEquals(PerformanceProfile.LOW_SPEC,
                PerformanceProfile.fromSettingsValue("LOW_SPEC"));
    }

    @Test
    public void testFromSettingsValueLowSpecCaseInsensitive() {
        Assert.assertEquals(PerformanceProfile.LOW_SPEC,
                PerformanceProfile.fromSettingsValue("low_spec"));
    }

    @Test
    public void testFromSettingsValueChromebook() {
        Assert.assertEquals(PerformanceProfile.CHROMEBOOK,
                PerformanceProfile.fromSettingsValue("CHROMEBOOK"));
    }

    @Test
    public void testGetSettingsValueRoundTrip() {
        for (PerformanceProfile profile : PerformanceProfile.values()) {
            Assert.assertEquals(profile,
                    PerformanceProfile.fromSettingsValue(profile.getSettingsValue()));
        }
    }
}
