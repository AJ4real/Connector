package me.aj4real.connector;

import javax.naming.OperationNotSupportedException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class Connector {

    public String auth = null;
    private EventHandler handler = new EventHandler();

    public void setAuthenticationBasic(String user, String pass) throws OperationNotSupportedException {
        String auth = user + ":" + pass;
        String authHeaderValue = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        this.auth = "Basic " + authHeaderValue;
    }
    public Response sendRequest(Request request) throws IOException {
        if (!request.isValid()) return null;
        return readJson(request.getUrl(), request.getRequestMethod(), request.serialize().toString());
    }
    public Response readJson(String strUrl, REQUEST_METHOD method, String data, Map<String,String> additionalHeaders) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod(method.name());
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Connector (www.adriftus.com/, 0.0.1)");
        con.setRequestProperty("Authorization", auth);
        if(additionalHeaders != null) {
            additionalHeaders.entrySet().forEach(e -> {
                con.setRequestProperty(e.getKey(), e.getValue());
            });
        }
        if (method == REQUEST_METHOD.PATCH) {
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        }
        con.setDoInput(true);
        if (data != null) {
            con.setDoOutput(true);
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (IOException e) {
                throw e;
            }
        }
        StringBuilder response = new StringBuilder();
        try {
            int statusCode = con.getResponseCode();
            InputStream is = null;
            if (statusCode >= 200 && statusCode < 400) {
                is = con.getInputStream();
            } else {
                is = con.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (IOException e) {
            throw e;
        }
        con.disconnect();
        Response r = new Response(this, con, response.toString());
        return r;
    }

    public Response readJson(String strUrl, REQUEST_METHOD method, String data) throws IOException { return readJson(strUrl, method, data, null); }

    public Response readJson(String strUrl, REQUEST_METHOD method) throws IOException { return readJson(strUrl, method, (String) null, null); /* casting fixes error */ }

    public Response readJson(String strUrl) throws IOException { return readJson(strUrl, REQUEST_METHOD.GET, null, null); }

    public EventHandler getHandler() {
        return this.handler;
    }

    public enum REQUEST_METHOD {
        GET,
        POST,
        HEAD,
        OPTIONS,
        PUT,
        DELETE,
        PATCH;
    }
}
