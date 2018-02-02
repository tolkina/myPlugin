package com.atlassian.tutorial.jira.ao.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.tutorial.jira.ao.domain.ProjectKeyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProjectKeyServiceImpl implements ProjectKeyService {
    private final ActiveObjects ao;
    private final ProjectManager projectManager;

    public ProjectKeyServiceImpl(ActiveObjects ao) {
        this.ao = ao;
        projectManager = ComponentAccessor.getProjectManager();
    }

    @Override
    public void saveAll(String[] projectKeys) {
        removeAll();
        if (projectKeys == null) return;
        for (String projectKey : projectKeys) {
            ProjectKeyEntity projectKeyEntity = ao.create(ProjectKeyEntity.class);
            projectKeyEntity.setProjectKey(projectKey);
            projectKeyEntity.save();
        }
    }

    @Override
    public void removeAll() {
        ao.delete(ao.find(ProjectKeyEntity.class));
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();
        Stream.of(ao.find(ProjectKeyEntity.class))
                .forEach(entity -> projects.add(projectManager.getProjectObjByKey(entity.getProjectKey())));
        return projects;
    }
}
