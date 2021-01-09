package me.aj4real.connector.github.paginatorconfigurations;

import me.aj4real.connector.paginators.Paginator;

public class ListRepositoryContributorsPaginatorConfiguration extends Paginator.Configuration {
    public ListRepositoryContributorsPaginatorConfiguration(long page) {
        super(page);
    }
    public void includeAnonymousContributors(boolean include) {
        query.put("anon", include);
    }
}
