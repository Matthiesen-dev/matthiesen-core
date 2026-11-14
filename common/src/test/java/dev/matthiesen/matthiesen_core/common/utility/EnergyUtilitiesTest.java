package dev.matthiesen.matthiesen_core.common.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergyUtilitiesTest {
    private Locale originalFormatLocale;

    @BeforeEach
    void setFormatLocale() {
        originalFormatLocale = Locale.getDefault(Locale.Category.FORMAT);
        Locale.setDefault(Locale.Category.FORMAT, Locale.US);
    }

    @AfterEach
    void restoreFormatLocale() {
        Locale.setDefault(Locale.Category.FORMAT, originalFormatLocale);
    }

    @Test
    void parsesEnergyWithAndWithoutUnits() {
        long[] values = {0, 1, 9, 10, 99, 100, 999, 1000, 1500, 1_000_000, 1_000_000_000,
                -1, -99, -1000, Long.MIN_VALUE};
        String[] expected = {"0", "1", "9", "10", "99", "100", "999", "1k", "1.5k", "1M", "1B",
                "-1", "-99", "-1000", String.valueOf(Long.MIN_VALUE)};

        for (int i = 0; i < values.length; i++) {
            assertEquals(expected[i], EnergyUtilities.toParsedString(values[i]), "energy: " + values[i]);
            assertEquals(expected[i], EnergyUtilities.toParsedString(values[i], false), "energy: " + values[i]);
            assertEquals(expected[i] + " FE", EnergyUtilities.toParsedString(values[i], true), "energy: " + values[i]);
        }
    }

    @Test
    void removesOnlyMatchingSuffixes() {
        String[][] cases = {
                {"1.0k", "1k"}, {"12.0M", "12M"}, {"-3.0B", "-3B"},
                {".0k", "k"}, {".0M", "M"}, {".0B", "B"},
                {"1.5k", "1.5k"}, {"1.0T", "1.0T"}, {"1,0k", "1,0k"},
                {"1.0k FE", "1.0k FE"}, {"", ""}, {"0", "0"}, {"99", "99"},
                {"-1", "-1"}, {"-99", "-99"}
        };
        for (String[] testCase : cases) {
            assertEquals(testCase[1], EnergyUtilities.formatEnergyValue(testCase[0]), "parsed: " + testCase[0]);
        }
    }

    @Test
    void preservesLocaleSpecificDecimalFormatting() {
        Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
        assertEquals("1,0k", EnergyUtilities.toParsedString(1000));
        assertEquals("1,5k FE", EnergyUtilities.toParsedString(1500, true));
    }
}
