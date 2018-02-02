package com.atlassian.tutorial.jira.ao.domain;

import net.java.ao.Entity;

public interface ProjectKeyEntity extends Entity {
    String getProjectKey();

    void setProjectKey(String projectKey);
}