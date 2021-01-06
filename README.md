# Connector
I wrote this API because I was bored and had nothing to do.
```java
GithubConnector github = new GithubConnector();
github.setPersonalAuthenticationToken("my token");
GithubUser user = github.getGithub().fetchCurrentUser().get();

//Listening for events
github.addHandler(WatchEvent.class, (e) -> {
    System.out.println("now watching " + e.getRepoName());
});
github.addHandler(CommitCommentEvent.class, (e) -> {
    System.out.println("you commented " + e.getComment().getBody() + " on a commit from " + e.getRepoName());
});
user.listen();

//Modifying a Repository
Paginator<List<GithubRepository>> paginator = user.getRepositories();
GithubRepository repo = paginator.next().get(0);
GithubRepository modifiedRepo = repo.modify((spec) -> {
    spec.setName("demo");
    spec.setDescription("this is my repository");
}).block();
```
