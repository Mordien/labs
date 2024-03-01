package com.magnusr.routes.model;

import com.magnusr.routes.model.entity.JourEntity;
import com.magnusr.routes.model.entity.StopEntity;
import com.magnusr.routes.model.response.JourResponse;
import com.magnusr.routes.model.response.ResponseData;
import com.magnusr.routes.model.response.StopResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestData {

    public static JourResponse GetSampleJourResponse() {
        var response = new JourResponse();
        response.StatusCode = "0";
        response.Message = null;
        response.ExecutionTime = "0";
        response.ResponseData = new ResponseData<>();
        response.ResponseData.Type = "JourneyPatternPointOnLine";
        response.ResponseData.Version = "2024-02-26 00:14";
        response.ResponseData.Result = getJourEntities();

        return response;
    }

    public static StopResponse GetSampleStopResponse() {
        var response = new StopResponse();
        response.StatusCode = "0";
        response.Message = null;
        response.ExecutionTime = "0";
        response.ResponseData = new ResponseData<>();
        response.ResponseData.Type = "StopPoint";
        response.ResponseData.Version = "2024-02-26 00:14";
        response.ResponseData.Result = getStopEntities();

        return response;
    }

    public static Map<String, List<String>> getSampleMapWithRoutesAndStops() {
        var map = new HashMap<String, List<String>>();
        map.put("Route 1", new ArrayList<>() {
            { add("Stop 1"); }
            { add("Stop 2"); }
            { add("Stop 3"); }
        });
        map.put("Route 2", new ArrayList<>() {
            { add("Stop 4"); }
            { add("Stop 4"); }
            { add("Stop 6"); }
        });
        map.put("Route 3", new ArrayList<>() {
            { add("Stop 7"); }
            { add("Stop 8"); }
        });

        return map;
    }

    private static JourEntity[] getJourEntities() {
        var jourEntities = new JourEntity[16];

        for (int i = 0; i < 16; i++) {
            var je = new JourEntity();
            je.JourneyPatternPointNumber = "11" + i;
            je.LineNumber = "" + (i + 1);
            jourEntities[i] = je;
        }

        return jourEntities;
    }


    private static StopEntity[] getStopEntities() {
        var stopEntities = new StopEntity[16];

        for(int i = 0; i < 16; i++) {
            var s1 = new StopEntity();
            s1.StopPointNumber = "11" + i;
            s1.StopPointName = "Stop name " + (i + 1);
        }

        return stopEntities;
    }

}
