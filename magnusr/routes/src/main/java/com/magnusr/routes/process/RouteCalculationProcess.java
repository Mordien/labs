package com.magnusr.routes.process;

import com.magnusr.routes.model.entity.JourEntity;
import com.magnusr.routes.model.entity.StopEntity;
import com.magnusr.routes.model.response.JourResponse;
import com.magnusr.routes.model.response.RouteWithStops;
import com.magnusr.routes.model.response.StopResponse;
import com.magnusr.routes.util.HttpUtils;
import com.magnusr.routes.util.PropertyUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.magnusr.routes.process.RouteCalculationUtils.mapToListWithRouteStops;
import static com.magnusr.routes.util.MapUtils.removeDuplicatesFromListValue;
import static java.util.stream.Collectors.toMap;

@Slf4j
/**
 * RouteCalculationProcess
 *
 * This class represents the main logic.
 * It fetches and aggregates data to return the top routes with most stops
 */
public class RouteCalculationProcess {

    static class DataCallable implements Callable<Long> {
        protected Long id;

        public DataCallable(Long val) {
            this.id = val;
        }

        public Long call() {
            return id;
        }
    }

    private String jourApi;
    private String stopApi;
    private int topNumberOfRoutesWithMostStops;
    private boolean errorOnReadingConfigValues = false;

    public RouteCalculationProcess() {
        getConfigValues();
    }

    /**
     * Fetches data from two endpoints, stops and routes, in parallel to aggregate
     * the data to get the top routes with most stops.
     */
    public List<RouteWithStops> FindRoutesWithMostStops() throws InterruptedException {
        if (errorOnReadingConfigValues) {
            log.error("Configuration values were not read properly");

            return emptyResult();
        }

        List<DataCallable> tasks = new ArrayList<>();
        StopResponse[] stopResponses = new StopResponse[1];
        JourResponse[] jourResponses = new JourResponse[1];
        int[] failures = { 0 };

        tasks.add(new DataCallable(0L) {
            public Long call() {
                try {
                    log.info("Getting all stop points");
                    stopResponses[0] = GetResultFromApi(stopApi, StopResponse.class);
                } catch (IOException | InterruptedException e) {
                    log.error("An error occurred getting all stop points {}", e.getMessage());
                    e.printStackTrace();
                    failures[0]++;
                }

                return id;
            }
        });
        tasks.add(new DataCallable(1L) {
            public Long call() {
                try {
                    log.info("Getting all jours");
                    jourResponses[0] = GetResultFromApi(jourApi, JourResponse.class);
                } catch (IOException | InterruptedException e) {
                    log.error("An error occurred getting all jours {}", e.getMessage());
                    e.printStackTrace();
                    failures[0]++;
                }

                return id;
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
        executorService.invokeAll(tasks);

        // TODO: React UI.
        // TODO: Tests.

        if (failures[0] > 0) {
            return emptyResult();
        }

        var topRoutesWithMostStops = prepareResponseData(
                jourResponses[0].ResponseData.Result,
                stopResponses[0].ResponseData.Result);

        return mapToListWithRouteStops(topRoutesWithMostStops);
    }

    private void getConfigValues() {
        final String RESOURCE_FILE_NAME = "config.properties";
        final String JOUR_API = "jourApi";
        final String STOP_API = "stopApi";
        final String TOP_NUMBER_OF_ROUTES_WITH_MOST_STOPS = "topNumberOfRoutesWithMostStops";

        try {
        Properties config = PropertyUtils.loadProperties(RESOURCE_FILE_NAME);
            jourApi = config.getProperty(JOUR_API);
            stopApi = config.getProperty(STOP_API);
            topNumberOfRoutesWithMostStops = Integer.parseInt(config.getProperty(TOP_NUMBER_OF_ROUTES_WITH_MOST_STOPS));
        } catch(IOException | NumberFormatException e) {
            errorOnReadingConfigValues = true;
        }
    }

    /**
     * In parallel: makes calls to find the top routes with most stops and converts
     * the list of stops to a map for quick search (later)
     */
    private Map<String, List<String>> prepareResponseData(JourEntity[] jours, StopEntity[] stops) throws InterruptedException {
        List<DataCallable> tasks = new ArrayList<>();
        var topRoutesWithMostStopsList = new ArrayList<Map<String, List<String>>>();
        var stopPointToStopNameMapList = new ArrayList<Map<String, String>>();

        log.info("Preparing response data");
        tasks.add(new DataCallable(0L) {
            public Long call() {
                topRoutesWithMostStopsList.add(DoFindRoutesWithMostStops(jours));

                return id;
            }
        });
        tasks.add(new DataCallable(1L) {
            public Long call() {
                stopPointToStopNameMapList.add(stopListToMap(stops));

                return id;
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
        executorService.invokeAll(tasks);

        var topRoutesWithMostStops = topRoutesWithMostStopsList.get(0);
        var stopPointToStopNameMap = stopPointToStopNameMapList.get(0);

        topRoutesWithMostStops
                .entrySet ()
                .parallelStream ()
                .forEach (entry -> {
                    int size = entry.getValue().size();
                    var stopPointList = entry.getValue();
                    for (int cnt = 0; cnt < size; cnt++) {
                        var index = cnt;
                        var JourneyPatternPointNumber = stopPointList.get(index);
                        var stopPointName = stopPointToStopNameMap.get(JourneyPatternPointNumber);
                        stopPointList.set(index, stopPointName);
                    }
                });

        // To avoid getting duplicates due to counting both directions.
        removeDuplicatesFromListValue(topRoutesWithMostStops);

        return topRoutesWithMostStops;
    }

    /**
     * Finds the top routes with most stops
     */
    private Map<String, List<String>> DoFindRoutesWithMostStops(JourEntity[] jours) {
        var routes = new ConcurrentHashMap<String, List<String>>();
        Arrays.stream(jours).parallel().forEach(j -> {
            if (!routes.containsKey(j.LineNumber)) {
                routes.put(j.LineNumber, new ArrayList<>());
            }
            routes.get(j.LineNumber).add(j.JourneyPatternPointNumber);
        });

        var sortedRoutes = routes
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(topNumberOfRoutesWithMostStops)
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        ConcurrentHashMap::new
                ));

        return sortedRoutes;
    }

    /**
     * converts the list of stops to a map for quick search (later)
     */
    private Map<String, String> stopListToMap(StopEntity[] stops) {
        var stopMap = new ConcurrentHashMap<String, String>();

        Arrays.stream(stops).parallel().forEach(j -> {
            stopMap.put(j.StopPointNumber, j.StopPointName);
        });

        return stopMap;
    }

    /**
     * Empty (default) result, in the case of failure
     */
    private List<RouteWithStops> emptyResult() {
        return new ArrayList<>();
    }

    /**
     * A client call to fetch either journeys or stops.
     */
    private <T> T GetResultFromApi(String uri, Class<T> clazz) throws IOException, InterruptedException {
        T result = HttpUtils
                .createHttpClient()
                .withGetRequest(uri, null)
                .execute(clazz);

        return result;
    }
}
