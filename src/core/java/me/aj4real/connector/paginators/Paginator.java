package me.aj4real.connector.paginators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Paginator<T> {
    private final Function<Integer, T> returnValue;
    private int page = 0;
    private Paginator(Function<Integer, T> returnValue) {
        this.returnValue = returnValue;
    }
    public static <T> Paginator<T> of(Function<Integer, T> returnValue) {
        return new Paginator(returnValue);
    }
    public T next() {
        page++;
        return returnValue.apply(page);
    }
    public void skip(int number) {
        page = page + number;
    }
    public static class Configuration {
        public static final Consumer<? super Configuration> DEFAULT = (c) -> c.setEntriesPerPage(100);
        protected Map<String, Object> query = new HashMap<>();
        public Configuration(long page) {
            query.put("page", page);
        }
        public void setEntriesPerPage(long perPage) {
            query.put("per_page", perPage);
        }
        public String buildQuery() {
            List<String> fields = new ArrayList<String>();
            for(Map.Entry<String, Object> e : query.entrySet()) {
                fields.add(e.getKey() + "=" + e.getValue());
            }
            return "?" + String.join("&", fields);
        }
        public enum SortDirection {
            ASCENDING("asc"),
            DESCENDING("desc");
            private String s;
            SortDirection(String s) {
                this.s = s;
            }
            public String getIdentifier() {
                return s;
            }
        }
    }

}
