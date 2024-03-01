package com.magnusr.routes.process;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.magnusr.routes.model.TestData.getSampleMapWithRoutesAndStops;

public class RouteCalculationUtilsTest {

    @Test
    void mapToListWithRouteStopsTest() {
        var map = getSampleMapWithRoutesAndStops();
        var list = RouteCalculationUtils.mapToListWithRouteStops(map);

        assertEquals(3, list.size());
        assertEquals(3, list.stream().filter(r -> r.getRoute().equals("Route 1"))
                .findFirst().get().getStops().size());
        assertEquals(3, list.stream().filter(r -> r.getRoute().equals("Route 2"))
                .findFirst().get().getStops().size());
        assertEquals(2, list.stream().filter(r -> r.getRoute().equals("Route 3"))
                .findFirst().get().getStops().size());
    }

}
