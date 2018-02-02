package com.atlassian.tutorial.jira.webwork;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.tutorial.jira.ao.service.ProjectKeyService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class MyWebworkModuleAction extends JiraWebActionSupport {
    private final JiraAuthenticationContext authenticationContext;
    private final IssueService issueService;
    private final ProjectKeyService projectKeyService;

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String summary;
    private IssueService.UpdateValidationResult updateValidationResult;

    public MyWebworkModuleAction(ProjectKeyService projectKeyService) {
        this.projectKeyService = projectKeyService;
        authenticationContext = ComponentAccessor.getJiraAuthenticationContext();
        issueService = ComponentAccessor.getIssueService();
    }

    public void doValidation() {
        if (summary.replaceAll("\\s+", "").equals("")) {
            this.addErrorMessage("Summary can't be empty.");
            return;
        }
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        issueInputParameters.setSummary(summary);
        updateValidationResult = issueService.validateUpdate(authenticationContext.getLoggedInUser(), this.id,
                issueInputParameters);
        if (!updateValidationResult.isValid()) {
            this.addErrorCollection(updateValidationResult.getErrorCollection());
        }
    }

    public String doExecute() throws Exception {
        IssueService.IssueResult update = issueService.update(authenticationContext.getLoggedInUser(),
                updateValidationResult);
        return !update.isValid() ? "error" : returnComplete();
    }

    public String doDefault() {
        MutableIssue issue = issueService.getIssue(authenticationContext.getLoggedInUser(), id).getIssue();
        if (issue == null) {
            return "error";
        }
        this.summary = issue.getSummary();
        return !issue.isSubTask() || !projectKeyService.getAll().contains(issue.getProjectObject()) ? "error" : "input";
    }
}
