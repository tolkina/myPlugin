package com.atlassian.tutorial.jira.ao.service;


import com.atlassian.jira.project.Project;

import java.util.List;

public interface ProjectKeyService {
    void saveAll(String[] projectKeys);

    void removeAll();

    List<Project> getAll();
}
