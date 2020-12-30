package me.aj4real.connector.github.events;

import me.aj4real.connector.github.events.repository.*;

public enum GithubRepositoryEvents {
    // https://docs.github.com/en/free-pro-team@latest/developers/webhooks-and-events/github-event-types
    GithubEvent(GithubRepositoryEvent.class),

    CommitCommentEvent(me.aj4real.connector.github.events.repository.CommitCommentEvent.class),
    CreateEvent(me.aj4real.connector.github.events.repository.CreateEvent.class),
//    DeleteEvent,
    ForkEvent(me.aj4real.connector.github.events.repository.ForkEvent.class),
//    GollumEvent,
//    IssueCommentEvent,
//    IssuesEvent,
//    MemberEvent,
    PublicEvent(me.aj4real.connector.github.events.repository.PublicEvent.class),
//    PullRequestEvent,
//    PullRequestReviewCommentEvent,
    PushEvent(me.aj4real.connector.github.events.repository.PushEvent.class),
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
