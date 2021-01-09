# Connector
I wrote this API because I was bored and had nothing to do.
```java
GithubConnector github = new GithubConnector();
github.setPersonalAuthenticationToken("my token");

// Get the current user
GithubAuthenticatedUser currentUser = github.getGithub().fetchCurrentUser().get();

// Get a User
GithubUser user = github.getGithub().fetchUser("Ph4i1ur3").get();

// Get a Repository
GithubRepository repo = github.getGithub().fetchRepository("AJ4real", "Connector").get();

// Get an organization
GithubOrganization org = github.getGithub().fetchOrganization("Adriftus Studios").get();

// List all publicly available contributors for a repository
Paginator<List<GithubPerson>> contributors = repo.listContributors((spec) -> {
    spec.includeAnonymousContributors(false);
    spec.setEntriesPerPage(100);
});
List<GithubPerson> page1 = contributors.next();
List<GithubPerson> page2 = contributors.next();
contributors.skip(2);
List<GithubPerson> page5 = contributors.next();

// Listening for events
github.addHandler(WatchEvent.class, (e) -> {
    System.out.println("now watching " + e.getRepoName());
});
github.addHandler(CommitCommentEvent.class, (e) -> {
    System.out.println("you commented " + e.getComment().getBody() + " on a commit from " + e.getRepoName());
});
currentUser.listen();
user.listen();
repo.listen();
org.listen();

// Creating a Repository
GithubRepository newlyCreatedRepository = currentUser.createRepository((spec) -> {
    spec.setName("My repository");
    spec.setDescription("Check out what I made");
    spec.setIsPrivate(false);
    spec.setHasDownloads(false);
    spec.setTeamId(123456);
}).block();

// Modifying a Repository
GithubRepository modifiedRepo = newlyCreatedRepository.modify((spec) -> {
    spec.setName("demo");
    spec.setDescription("this is my repository");
}).block();

// Create an Issue for a Repository
repo.createIssue((spec) -> {
    spec.setTitle("I found a bug");
    spec.setBody("...");
});

// List issues for a Repository
Paginator<List<GithubIssue>> issues = repo.listIssues((config) -> {
    config.after(new Date(System.currentTimeMillis() - 604800000)); // all issues within 7 days of now
    config.setState(ListRepositoryIssuesPaginatorConfiguration.State.OPEN);
    config.setSortDirection(ListRepositoryIssuesPaginatorConfiguration.SortDirection.DESCENDING);
    config.setSortType(ListRepositoryIssuesPaginatorConfiguration.SortType.CREATED);
});
List<GithubIssue> page1 = issues.next();
List<GithubIssue> page2 = issues.next();
```
