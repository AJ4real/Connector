package me.aj4real.connector.github.paginatorconfigurations;

import me.aj4real.connector.paginators.Paginator;

public class ListBranchesPaginatorConfiguration extends Paginator.Configuration {
    public ListBranchesPaginatorConfiguration(long page) {
        super(page);
    }
    public void setProtected(boolean isProtected) {
        query.put("protected", isProtected);
    }
}
