package com.magnusr.routes.process;

import com.magnusr.routes.model.response.RouteWithStops;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteCalculationUtils {

    public static List<RouteWithStops> mapToListWithRouteStops(Map<String, List<String>> map) {
        var routeWithStopsList = new ArrayList<RouteWithStops>();

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            RouteWithStops rws = new RouteWithStops();
            rws.setRoute(entry.getKey());
            rws.setStops(entry.getValue());
            routeWithStopsList.add(rws);
        }

        return routeWithStopsList;
    }

}
