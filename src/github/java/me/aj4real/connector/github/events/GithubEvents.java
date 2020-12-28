package me.aj4real.connector.github.events;

public enum GithubEvents {
    WatchEvent(WatchEvent.class);
//    CommitCommentEvent,
//    CreateEvent,
//    DeleteEvent,
//    ForkEvent,
//    GollumEvent,
//    IssueCommentEvent,
//    IssuesEvent,
//    MemberEvent,
//    PublicEvent,
//    PullRequestEvent,
//    PullRequestReviewCommentEvent,
//    PushEvent,
//    ReleaseEvent,
//    SponsorshipEvent,
//    WatchEvent;
    Class c;
    GithubEvents(Class c) {
        this.c = c;
    }
    public Class getEventClass() {
        return this.c;
    }
}
