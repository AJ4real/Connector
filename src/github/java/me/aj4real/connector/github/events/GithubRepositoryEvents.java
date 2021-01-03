package me.aj4real.connector.github.events;

import me.aj4real.connector.github.events.repository.*;

public enum GithubRepositoryEvents {
    // https://docs.github.com/en/free-pro-team@latest/developers/webhooks-and-events/github-event-types
    GithubEvent(GithubRepositoryEvent.class),

    CommitCommentEvent(CommitCommentEvent.class),
    CreateEvent(CreateEvent.class),
    DeleteEvent(DeleteEvent.class),
    ForkEvent(ForkEvent.class),
//    GollumEvent,
    IssueCommentEvent(IssueCommentEvent.class),
//    IssuesEvent,
//    MemberEvent,
    PublicEvent(PublicEvent.class),
//    PullRequestEvent,
//    PullRequestReviewCommentEvent,
    PushEvent(PushEvent.class),
//    ReleaseEvent,
//    SponsorshipEvent,
    WatchEvent(WatchEvent.class);
    Class c;
    GithubRepositoryEvents(Class c) {
        this.c = c;
    }
    public Class getEventClass() {
        return this.c;
    }
}
