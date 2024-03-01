package com.magnusr.routes.model.response;

import com.magnusr.routes.model.entity.JourEntity;

/**
 * JourResponse
 *
 * Typed model to represent a response from:
 * https://api.sl.se/api2/LineData.json?key=[key]&model=jour&DefaultTransportModeCode=BUS
 */
public class JourResponse extends Response<JourEntity> {
    public JourResponse() {

    }
}
