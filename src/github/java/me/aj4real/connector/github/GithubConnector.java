package me.aj4real.connector.github;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Endpoint;
import me.aj4real.connector.events.Event;
import me.aj4real.connector.Response;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GithubConnector extends Connector {

    private Github github = null;

    public GithubConnector() {
        github = new Github(this);
    }

    public void setPersonalAuthenticationToken(String token) {
        auth = "token " + token;
    }

    @Override
    public void setAuthenticationBasic(String user, String pass) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Github does not support basic password authentication to their API.");
    }

    public Response readJson(Endpoint endpoint, String data) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github.v3+json");
        return readJson(endpoint, data, headers);
    }

    public Response readJson(Endpoint endpoint) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github.v3+json");
        return readJson(endpoint, null, headers);
    }
    public Response readJson(Endpoint endpoint, String data, GithubApiPreviews preview) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github." + preview.getKey() + "+json");
        return super.readJson(endpoint, data, headers);
    }

    public Response readJson(Endpoint endpoint, GithubApiPreviews preview) throws IOException {
        return readJson(endpoint, null, preview);
    }

    public Github getGithub() {
        return this.github;
    }

    public <T extends Event> void addHandler(Class<T> eventClass, Consumer<T> consumer) {
        getHandler().subscribe(eventClass, consumer);
    }
    public static Date getTimestamp(String strTime) {
        try {
            int year = Integer.valueOf(strTime.substring(0, 4));
            int month = Integer.valueOf(strTime.substring(5, 7));
            int day = Integer.valueOf(strTime.substring(8, 10));
            int hour = Integer.valueOf(strTime.substring(11, 13));
            int minute = Integer.valueOf(strTime.substring(14, 16));
            int second = Integer.valueOf(strTime.substring(17, 19));
            return new Date(year-1900, month, day, hour, minute, second);
        } catch (Exception e) {
            return null;
        }
    }
    public static String getTimestamp(Date date) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date).replace(" ", "T") + "Z";
    }
}
