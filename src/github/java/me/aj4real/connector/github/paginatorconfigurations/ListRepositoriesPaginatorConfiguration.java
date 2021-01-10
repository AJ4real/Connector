package me.aj4real.connector.github.paginatorconfigurations;

import me.aj4real.connector.paginators.Paginator;

public class ListRepositoriesPaginatorConfiguration extends Paginator.Configuration {
    public ListRepositoriesPaginatorConfiguration(long page) {
        super(page);
    }
    public void setType(Type type) {
        query.put("type", type.name().toLowerCase());
    }
    public void setSortType(SortType type) {
        query.put("sort", type.name().toLowerCase());
    }
    public void setSortDirection(SortDirection dir) {
        query.put("direction", dir.getIdentifier());
    }
    public enum SortType {
        CREATED,
        UPDATED,
        PUSHED,
        FULL_NAME;
    }
    public enum Type {
        ALL,
        OWNER,
        MEMBER;
    }
}
