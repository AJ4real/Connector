package me.aj4real.connector.github.specs;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Request;
import org.json.simple.JSONObject;

public class ModifyRepositorySpec extends Request {
    private String name, description, homepage, defaultBranch;
    private int teamId = -1;
    private boolean isPrivate, hasIssues, hasProjects, hasWiki, isTemplate, autoInit, allowSquashMerge, allowMergeCommit, allowRebaseMerge, deleteBranchOnMerge, hasDownloads, isArchived;
    private boolean _teamId, _isPrivate, _hasIssues, _hasProjects, _hasWiki, _isTemplate, _autoInit, _allowSquashMerge, _allowMergeCommit, _allowRebaseMerge, _deleteBranchOnMerge, _hasDownloads, _isArchived = false;
    public ModifyRepositorySpec(String url) {
        super(url);
    }

    public void setName(String value) {
        this.name = value;
    }
    public void setDescription(String value) {
        this.description = value;
    }
    public void setHomepage(String value) {
        this.homepage = value;
    }
    public void setDefaultBranch(String value) { this.defaultBranch = value; }
    public void setTeamId(int value) {
        this.teamId =   value;
        this._teamId = true;
    }
    public void setIsPrivate(boolean value) {
        this.isPrivate = value;
        this._isPrivate = true;
    }
    public void setHasIssues(boolean value) {
        this.hasIssues = value;
        this._hasIssues = true;
    }
    public void setHasProjects(boolean value) {
        this.hasProjects = value;
        this._hasProjects = true;
    }
    public void setHasWiki(boolean value) {
        this.hasWiki = value;
        this._hasWiki = true;
    }
    public void setHasDownloads(boolean value) {
        this.hasDownloads = value;
        this._hasDownloads = true;
    }
    public void setIsTemplate(boolean value) {
        this.isTemplate = value;
        this._isTemplate = true;
    }
    public void AutoInit(boolean value) {
        this.autoInit = value;
        this._autoInit = true;
    }
    public void AllowSquashMerge(boolean value) {
        this.allowSquashMerge = value;
        this._allowSquashMerge = true;
    }
    public void AllowMergeCommit(boolean value) {

        this.allowMergeCommit = value;
        this._allowMergeCommit = true;
    }
    public void AllowRebaseMerge(boolean value) {
        this.allowRebaseMerge = value;
        this._allowRebaseMerge = true;
    }
    public void deleteBranchOnMerge(boolean value) {
        this.deleteBranchOnMerge = value;
        this._deleteBranchOnMerge = true;
    }
    public void isArchived(boolean value) {
        this.isArchived = value;
        this._isArchived = true;
    }
    @Override
    public boolean isValid() {
        return name != null;
    }

    @Override
    public JSONObject serialize() {
        JSONObject data = new JSONObject();
        if (name != null) data.put("name", name);
        if (description != null) data.put("description", description);
        if (homepage != null) data.put("homepage", homepage);
        if (defaultBranch != null) data.put("default_branch", defaultBranch);
        if (_teamId) data.put("team_id", teamId);
        if (_isPrivate) data.put("private", isPrivate);
        if (_hasIssues) data.put("has_issues", hasIssues);
        if (_hasProjects) data.put("has_projects", hasProjects);
        if (_hasWiki) data.put("has_wiki", hasWiki);
        if (_hasDownloads) data.put("has_downloads", hasDownloads);
        if (_isTemplate) data.put("is_template", isTemplate);
        if (_autoInit) data.put("auto_init", autoInit);
        if (_allowSquashMerge) data.put("allow_squash_merge", allowSquashMerge);
        if (_allowMergeCommit) data.put("allow_merge_commit", allowMergeCommit);
        if (_allowRebaseMerge) data.put("allow_rebase_merge", allowRebaseMerge);
        if (_deleteBranchOnMerge) data.put("delete_branch_on_merge", deleteBranchOnMerge);
        if (_isArchived) data.put("archived", isArchived);
        return data;
    }
    @Override
    public Connector.REQUEST_METHOD getRequestMethod() {
        return Connector.REQUEST_METHOD.PATCH;
    }

}
