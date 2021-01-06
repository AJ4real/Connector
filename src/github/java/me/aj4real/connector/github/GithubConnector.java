package me.aj4real.connector.github;

import me.aj4real.connector.Connector;
import me.aj4real.connector.events.Event;
import me.aj4real.connector.Response;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
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

    public Response readJson(String strUrl, REQUEST_METHOD method, String data) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github.v3+json");
        return readJson(strUrl, method, data, headers);
    }

    public Response readJson(String strUrl, REQUEST_METHOD method) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github.v3+json");
        return readJson(strUrl, method, (String) null, headers); /* casting fixes error */
    }

    public Response readJson(String strUrl) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github.v3+json");
        return readJson(strUrl, REQUEST_METHOD.GET, null, headers);
    }

    public Response readJson(String strUrl, REQUEST_METHOD method, String data, GithubApiPreviews preview) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/vnd.github." + preview.getKey() + "+json");
        return super.readJson(strUrl, method, data, headers);
    }

    public Response readJson(String strUrl, REQUEST_METHOD method, GithubApiPreviews preview) throws IOException {
        return readJson(strUrl, method, null, preview);
    }

    public Response readJson(String strUrl, GithubApiPreviews preview) throws IOException {
        return readJson(strUrl, REQUEST_METHOD.GET, null, preview);
    }

    public Github getGithub() {
        return this.github;
    }

    public <T extends Event> void addHandler(Class<T> eventClass, Consumer<T> consumer) {
        getHandler().subscribe(eventClass, consumer);
    }
    public static Date getDate(String strTime) {
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
}
