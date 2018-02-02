package com.atlassian.tutorial.jira.webwork;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.tutorial.jira.ao.service.ProjectKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConfigureSubtaskPluginWebworkAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(ConfigureSubtaskPluginWebworkAction.class);

    private final ProjectKeyService projectKeyService;
    private List<Project> projects;
    private List<Project> savedProjects;
    private String[] selectedProjectKeys;

    public ConfigureSubtaskPluginWebworkAction(ProjectKeyService projectKeyService) {
        this.projectKeyService = projectKeyService;
    }

    @Override
    protected String doExecute() throws Exception {
        projectKeyService.saveAll(selectedProjectKeys);
        return super.doExecute();
    }

    @Override
    public String doDefault() throws Exception {
        projects = ComponentAccessor.getProjectManager().getProjectObjects();
        savedProjects = projectKeyService.getAll();
        return super.doDefault();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getSavedProjects() {
        return savedProjects;
    }

    public void setSavedProjects(List<Project> savedProjects) {
        this.savedProjects = savedProjects;
    }

    public String[] getSelectedProjectKeys() {
        return selectedProjectKeys;
    }

    public void setSelectedProjectKeys(String[] selectedProjectKeys) {
        this.selectedProjectKeys = selectedProjectKeys;
    }
}
