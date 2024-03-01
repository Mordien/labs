package com.magnusr.routes.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyUtilsTest {

    @Test
    void testPropertyUtils() {
        String jourApi = "";
        String stopApi = "";
        int topNumberOfRoutesWithMostStops = -1;

        try {
            Properties config = PropertyUtils.loadProperties("config.properties");
            jourApi = config.getProperty("jourApi");
            stopApi = config.getProperty("stopApi");
            topNumberOfRoutesWithMostStops = Integer.parseInt(config.getProperty("topNumberOfRoutesWithMostStops"));
        } catch(IOException | NumberFormatException e) {
            // Nothing
        }

        assertEquals(
                "https://api.sl.se/api2/LineData.json?key=be939f8dded64252b3a4a1e7699f8f69&model=jour&DefaultTransportModeCode=BUS",
                jourApi);
        assertEquals(
                "https://api.sl.se/api2/LineData.json?key=be939f8dded64252b3a4a1e7699f8f69&model=stop&DefaultTransportModeCode=BUS",
                stopApi);
        assertEquals(10, topNumberOfRoutesWithMostStops);
    }

}
