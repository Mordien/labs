package com.magnusr.routes.model.response;

/**
 * ResponseData
 *
 * Typed model to represent the data, specifically "Result", in the response from:
 * https://api.sl.se/api2/LineData.json?key=[key]&model=[model]&DefaultTransportModeCode=BUS
 */
public class ResponseData<T> {

    public ResponseData() {

    }

    public String Version;
    public String Type;
    public T[] Result;
}
