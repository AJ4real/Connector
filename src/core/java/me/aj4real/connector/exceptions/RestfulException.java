package me.aj4real.connector.exceptions;

import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Response;

public class RestfulException extends Exception {
    private final Endpoint endpoint;
    private final Response response;
    private final String message;
    public RestfulException(Endpoint endpoint, Response response, String message) {
        this.endpoint = endpoint;
        this.response = response;
        this.message = message;
    }
    public RestfulException(Endpoint endpoint, Response response) {
        this(endpoint, response, response.getResponseMessage());
    }

    public Endpoint getEndpoint() {
        return this.endpoint;
    }

    public Response getResponse() {
        return this.response;
    }

    public String getMessage() {
        return this.message;
    }
}
