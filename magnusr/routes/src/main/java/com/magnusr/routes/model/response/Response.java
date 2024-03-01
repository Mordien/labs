package com.magnusr.routes.model.response;

/**
 * Response
 *
 * Abstract model to represent a response from:
 * https://api.sl.se/api2/LineData.json?key=[key]&model=[model]&DefaultTransportModeCode=BUS
 */
public abstract class Response<T> {

    public Response() {

    }

    public String StatusCode;
    public String Message;
    public String ExecutionTime;
    public ResponseData<T> ResponseData;
}
