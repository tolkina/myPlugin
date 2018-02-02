package com.atlassian.tutorial.jira.conditions;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.tutorial.jira.ao.service.ProjectKeyService;

public class iaAllowByConfigCondition extends AbstractIssueWebCondition {
    private final ProjectKeyService projectKeyService;

    public iaAllowByConfigCondition(ProjectKeyService projectKeyService) {
        this.projectKeyService = projectKeyService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return projectKeyService.getAll().contains(issue.getProjectObject());
    }
}
