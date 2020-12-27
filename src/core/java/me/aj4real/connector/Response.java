package me.aj4real.connector;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class Response {
    private Connector c;
    private Object data;
    private Map<String, List<String>> headers;
    private HttpURLConnection con;
    public Response(Connector c, HttpURLConnection con, String data) {
        this.c = c;
        this.con = con;
        this.headers = con.getHeaderFields();
        try {
            this.data = JSONValue.parseWithException(data);
        } catch (ParseException e) {
            this.data = null;
        }
    }

    public int getResponseCode() {
        try {
            return this.con.getResponseCode();
        } catch (IOException e) {
            return 400;
        }
    }

    public Connector getConnector() {
        return this.c;
    }
    public Object getData() {
        return this.data;
    }
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }
}
