package me.aj4real.connector.github.objects;

import me.aj4real.connector.github.GithubConnector;
import org.json.simple.JSONObject;

public class Milestone {
    private final GithubConnector c;
    private final JSONObject data;
    public Milestone(GithubConnector c, JSONObject data) {
        this.c = c;
        this.data = data;
    }
    /*
    {
    "url": "https://api.github.com/repos/Adriftus-Studios/network-script-data/milestones/2",
    "html_url": "https://github.com/Adriftus-Studios/network-script-data/milestone/2",
    "labels_url": "https://api.github.com/repos/Adriftus-Studios/network-script-data/milestones/2/labels",
    "id": 5828229,
    "node_id": "MDk6TWlsZXN0b25lNTgyODIyOQ==",
    "number": 2,
    "title": "2020 - Year Progress",
    "description": "Annual Progress of Adriftus Scripts",
    "creator": {
      "login": "BehrRiley",
      "id": 46008563,
      "node_id": "MDQ6VXNlcjQ2MDA4NTYz",
      "avatar_url": "https://avatars3.githubusercontent.com/u/46008563?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/BehrRiley",
      "html_url": "https://github.com/BehrRiley",
      "followers_url": "https://api.github.com/users/BehrRiley/followers",
      "following_url": "https://api.github.com/users/BehrRiley/following{/other_user}",
      "gists_url": "https://api.github.com/users/BehrRiley/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/BehrRiley/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/BehrRiley/subscriptions",
      "organizations_url": "https://api.github.com/users/BehrRiley/orgs",
      "repos_url": "https://api.github.com/users/BehrRiley/repos",
      "events_url": "https://api.github.com/users/BehrRiley/events{/privacy}",
      "received_events_url": "https://api.github.com/users/BehrRiley/received_events",
      "type": "User",
      "site_admin": false
    },
    "open_issues": 1,
    "closed_issues": 10,
    "state": "open",
    "created_at": "2020-08-31T15:31:02Z",
    "updated_at": "2021-01-04T16:19:05Z",
    "due_on": "2020-12-31T08:00:00Z",
    "closed_at": null
  },
     */
}
