package com.magnusr.routes.model.response;

import com.magnusr.routes.model.entity.StopEntity;

/**
 * StopResponse
 *
 * Typed model to represent a response from:
 * https://api.sl.se/api2/LineData.json?key=[key]&model=stop&DefaultTransportModeCode=BUS
 */
public class StopResponse extends Response<StopEntity> {
    public StopResponse() {

    }
}
