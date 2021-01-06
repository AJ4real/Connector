package me.aj4real.connector.github.events;

import me.aj4real.connector.github.events.repository.*;

public enum GithubRepositoryEvents {
    // https://docs.github.com/en/free-pro-team@latest/developers/webhooks-and-events/github-event-types
    GithubEvent(GithubEvent.class),
    GithubRepositoryEvent(GithubRepositoryEvent.class),
    CommitCommentEvent(CommitCommentEvent.class),
    CreateEvent(CreateEvent.class),
    DeleteEvent(DeleteEvent.class),
    ForkEvent(ForkEvent.class),
    GollumEvent(GollumEvent.class),
    IssueCommentEvent(IssueCommentEvent.class),
    IssuesEvent(IssuesEvent.class),
    MemberEvent(MemberEvent.class),
    PublicEvent(PublicEvent.class),
    PullRequestEvent(PullRequestEvent.class),
    PullRequestReviewCommentEvent(PullRequestReviewCommentEvent.class), //TODO
    PushEvent(PushEvent.class),
    ReleaseEvent(ReleaseEvent.class), //TODO
    SponsorshipEvent(null),
    WatchEvent(WatchEvent.class);
    Class<? extends GithubEvent> c;
    GithubRepositoryEvents(Class<? extends GithubEvent> c) {
        this.c = c;
    }
    public Class getEventClass() {
        return this.c;
    }
}
