package me.aj4real.connector.discord.pagInatorconfigurations;

import me.aj4real.connector.paginators.Paginator;

public class ListGuildMembersPaginatorConfiguration extends Paginator.Configuration {
    public ListGuildMembersPaginatorConfiguration(long after) {
        super(after);
        query.put("after", String.valueOf(after));
    }
    @Override
    public void setEntriesPerPage(long perPage) {
        query.put("limit", perPage);
    }
}
