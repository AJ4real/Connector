package me.aj4real.connector;

import me.aj4real.connector.events.EventHandler;
import sun.misc.Unsafe;

import javax.naming.OperationNotSupportedException;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Connector {

    protected String auth = null;
    protected String userAgent = "Connector (www.adriftus.com/, 0.0.1)";
    private EventHandler handler = new EventHandler();

    static {
        try {
            allowHttpMethods("PATCH");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAuthenticationBasic(String user, String pass) throws OperationNotSupportedException {
        String auth = user + ":" + pass;
        String authHeaderValue = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        this.auth = "Basic " + authHeaderValue;
    }
    public Response sendRequest(Request request) throws IOException {
        if (!request.isValid()) return null;
        return readJson(request.getEndpoint(), request.serialize().toString());
    }
    public Response readJson(Endpoint endpoint, String data, Map<String,String> additionalHeaders) throws IOException {
        if (!endpoint.isComplete()) return null;
        URL url = new URL(endpoint.getUrl());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Authorization", auth);
        con.setRequestMethod(endpoint.getHttpMethod().name());
        if(additionalHeaders != null) {
            additionalHeaders.entrySet().forEach(e -> {
                con.setRequestProperty(e.getKey(), e.getValue());
            });
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

    public Response readJson(Endpoint endpoint, String data) throws IOException { return readJson(endpoint, data, null); }

    public Response readJson(Endpoint endpoint) throws IOException { return readJson(endpoint, (String) null, null); }

    public EventHandler getHandler() {
        return this.handler;
    }

    public static void allowHttpMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
            methodsField.setAccessible(true);
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Object staticFieldBase = unsafe.staticFieldBase(methodsField);
            final long staticFieldOffset = unsafe.staticFieldOffset(methodsField);
            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);
            unsafe.putObject(staticFieldBase, staticFieldOffset, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
