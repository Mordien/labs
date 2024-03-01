package com.magnusr.routes.model.response;

import lombok.Data;

import java.util.List;

@Data
public class RouteWithStops {
    private String route;
    private List<String> stops;
}
