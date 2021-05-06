package me.aj4real.connector;

public class Endpoint {
    public enum HttpMethod {
        OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT, PATCH, OTHER
    }
    private HttpMethod method;
    private String route;
    private String[] requiredKeys;
    private String[] optionalKeys;
    public Endpoint(final HttpMethod method, final String route, final String requiredKeys) {
        this(method, route, requiredKeys, new String[] { requiredKeys });
    }
    public Endpoint(final HttpMethod method, final String route, final String requiredKeys, final String[] optionalKeys) {
        this(method, route, new String[] { requiredKeys }, optionalKeys);
    }
    public Endpoint(final HttpMethod method, final String route) {
        this(method, route, (String) null);
    }
    public Endpoint(final HttpMethod method, final String route, final String[] requiredKeys) {
        this(method, route, requiredKeys, null);
    }
    public Endpoint(final HttpMethod method, final String route, final String[] requiredKeys, final String[] optionalKeys) {
        this.method = method;
        this.route = route;
        this.requiredKeys = requiredKeys;
        this.optionalKeys = optionalKeys;
    }

    public Endpoint fulfil(String key, String value) {
        if (value == null) return new Endpoint(method, route, requiredKeys, optionalKeys);
        return new Endpoint(method, route.replace("{" + key + "}", value), requiredKeys, optionalKeys);
    }

    public Endpoint addQuery(String query) {
        return new Endpoint(method, route + query, requiredKeys, optionalKeys);
    }

    public boolean isComplete() {
        for(String key : requiredKeys) {
            if(route.contains("{" + key + "}")) {
                return false;
            }
        }
        return true;
    }

    public String getUrl() {
        String url = route;
        for(String opKey : optionalKeys) {
            url = url.replace("/{" + opKey + "}", "");
        }
        return url;
    }

    public HttpMethod getHttpMethod() {
        return this.method;
    }

}
