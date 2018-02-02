package com.atlassian.tutorial.jira.webwork;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.workflow.IssueWorkflowManager;
import com.atlassian.jira.workflow.TransitionOptions;
import com.atlassian.tutorial.jira.ao.service.ProjectKeyService;
import com.opensymphony.workflow.loader.ActionDescriptor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.Collection;

@Log4j
public class ChangeStatusWebworkModuleAction extends JiraWebActionSupport {
    private final JiraAuthenticationContext authenticationContext;
    private final IssueService issueService;
    private final ProjectKeyService projectKeyService;
    private final IssueWorkflowManager issueWorkflowManager;

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Collection<ActionDescriptor> actions;
    @Getter
    @Setter
    private Integer selectedActionId;
    private IssueService.TransitionValidationResult validationResult;

    public ChangeStatusWebworkModuleAction(ProjectKeyService projectKeyService, IssueWorkflowManager issueWorkflowManager) {
        this.projectKeyService = projectKeyService;
        this.issueWorkflowManager = issueWorkflowManager;
        authenticationContext = ComponentAccessor.getJiraAuthenticationContext();
        issueService = ComponentAccessor.getIssueService();
    }

    public void doValidation() {
        System.out.println(selectedActionId);
        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
        validationResult = issueService.validateTransition(authenticationContext.getLoggedInUser(), id,
                        selectedActionId, issueInputParameters);

    }

    public String doExecute() throws Exception {
        if (!validationResult.isValid()) {
            this.addErrorCollection(validationResult.getErrorCollection());
            return "error";
        }

        IssueService.IssueResult transition = issueService.transition(authenticationContext.getLoggedInUser(), validationResult);
        return !transition.isValid() ? "error" : returnComplete();
    }

    public String doDefault() {
        MutableIssue issue = issueService.getIssue(authenticationContext.getLoggedInUser(), id).getIssue();
        if (issue == null) {
            return "error";
        }
        actions = issueWorkflowManager.getAvailableActions(issue, authenticationContext.getLoggedInUser());
        return !issue.isSubTask() || !projectKeyService.getAll().contains(issue.getProjectObject()) ? "error" : "input";
    }
}
