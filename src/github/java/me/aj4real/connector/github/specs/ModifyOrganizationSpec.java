package me.aj4real.connector.github.specs;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Request;
import org.json.simple.JSONObject;

public class ModifyOrganizationSpec extends Request {
    private String billingEmail, company, email, twitterUsername, location, name, description, repositoryPermissions, blog;
    private boolean hasOrganizationProjects, hasRepositoryProjects, membersCanCreateRepositories, membersCanCreateInternalRepositories, membersCanCreatePrivateRepositories, membersCanCreatePublicRepositories, membersCanCreatePages;

    public ModifyOrganizationSpec(String url) {
        super(url);
    }

    public void setBillingEmail(String value) {
        this.billingEmail = value;
    }
    public void setCompany(String value) {
        this.company = value;
    }
    public void setEmail(String value) {
        this.email = value;
    }
    public void setTwitterUsername(String value) {
        this.twitterUsername = value;
    }
    public void setLocation(String value) {
        this.location = value;
    }
    public void setName(String value) {
        this.name = name;
    }
    public void setDescription(String value) {
        this.description = value;
    }
    public void setMemberRepositoryPermissions(RepositoryPermissions value) {
        this.repositoryPermissions = value.getId();
    }
    public void setHasOrganizationProjects(boolean value) {
        this.hasOrganizationProjects = value;
    }
    public void setHasRepositoryProjects(boolean value) {
        this.hasRepositoryProjects = value;
    }
    public void allowMembersToCreateRepositories(boolean value) {
        this.membersCanCreateRepositories = true;
    }
    public void allowMembersToCreateInternalRepositories(boolean value) {
        this.membersCanCreateInternalRepositories = true;
    }
    public void allowMembersToCreatePrivateRepositories(boolean value) {
        this.membersCanCreatePrivateRepositories = true;
    }
    public void allowMembersToCreatePublicRepositories(boolean value) {
        this.membersCanCreatePublicRepositories = true;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public JSONObject serialize() {
        JSONObject data = new JSONObject();
        data.put("billing_email", billingEmail);
        data.put("company", company);
        data.put("email", email);
        data.put("twitter_username", twitterUsername);
        data.put("location", location);
        data.put("name", name);
        data.put("description", description);
        data.put("has_organization_projects", hasOrganizationProjects);
        data.put("has_repository_projects", hasRepositoryProjects);
        data.put("default_repository_permission", repositoryPermissions);
        data.put("members_can_create_repositories", membersCanCreateRepositories);
        data.put("members_can_create_internal_repositories", membersCanCreateInternalRepositories);
        data.put("members_can_create_public_repositories", membersCanCreatePublicRepositories);
        data.put("members_can_create_private_repositories", membersCanCreatePrivateRepositories);
        data.put("members_can_create_pages", membersCanCreatePages);
        data.forEach((k,v) -> {
            if (v == null) data.remove(k);
        });
        return data;
    }
    @Override
    public Connector.REQUEST_METHOD getRequestMethod() {
        return Connector.REQUEST_METHOD.PATCH;
    }
    public enum RepositoryPermissions {
        READ("read"),
        READ_WRITE("write"),
        READ_WRITE_ADMIN("admin"),
        NONE("none");

        private final String id;
        RepositoryPermissions(String id) {
            this.id = id;
        }
        public String getId() {
            return this.id;
        }
    }
}
