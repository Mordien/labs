package com.magnusr.routes.controller;

import com.magnusr.routes.model.response.RouteWithStops;
import com.magnusr.routes.process.RouteCalculationProcess;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
@Slf4j
@Validated
@Data
/**
 * RouteController
 *
 * REST API that fetches and returns a response with the top routes with most stops
 *
 * Returns a map with routes and all their stops
 */
public class RouteController {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RouteWithStops>> getRoutes() throws InterruptedException {
        var process = new RouteCalculationProcess();
        List<RouteWithStops> routeWithStopsList = null;

        try {
            routeWithStopsList = process.FindRoutesWithMostStops();
        } catch (InterruptedException e) {
            log.error("An error occurred {},", e.getMessage());
        }

        return ResponseEntity.ok(routeWithStopsList);
    }
	
}
