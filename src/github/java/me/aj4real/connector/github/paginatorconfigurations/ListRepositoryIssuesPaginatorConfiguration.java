package me.aj4real.connector.github.paginatorconfigurations;

import me.aj4real.connector.github.GithubConnector;
import me.aj4real.connector.github.objects.GithubUser;
import me.aj4real.connector.paginators.Paginator;

import java.util.Date;

public class ListRepositoryIssuesPaginatorConfiguration extends Paginator.Configuration {
    public ListRepositoryIssuesPaginatorConfiguration(long page) {
        super(page);
    }
    public void setState(State state) {
        query.put("state", state.name().toLowerCase());
    }
    public void setCreator(String loginName) {
        query.put("creator", loginName);
    }
    public void setAssignee(String loginName) {
        // can be "none" for issues with no assigned user, and "*" for issues assigned to any user.
        query.put("assignee", loginName);
    }
    public void setMentioned(String loginName) {
        query.put("mentioned", loginName);
    }
    public void setSortDirection(SortDirection dir) {
        query.put("direction", dir.getIdentifier());
    }
    public void after(Date since) {
        query.put("since", GithubConnector.getTimestamp(since));
    }
    public void setSortType(SortType type) {
        query.put("sort", type.name().toLowerCase());
    }
    public void setLabels(String... labels) {
        query.put("labels", String.join(",", labels));
    }
    public enum State {
        OPEN,
        CLOSED,
        ALL;
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
    public enum SortType {
        CREATED,
        UPDATED,
        COMMENTS;
    }
}
