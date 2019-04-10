package org.inugami.core.providers.jenkins.models;

import flexjson.JSON;

import java.util.List;

public class JenkinsJob {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    @JSON(name="_class")
    private String classAtt;

    private String name;
    private String url;
    private String color;
    
    private List<JenkinsJob> jobs;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public JenkinsJob() {}

    public JenkinsJob(String classAtt, String name, String url, String color) {
        this.classAtt = classAtt;
        this.name = name;
        this.url = url;
        this.color = color;
    }

    // =========================================================================
    // OVERRIDE
    // =========================================================================

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("JenkinsJob: [_class=");
        builder.append(classAtt);
        builder.append(", name=");
        builder.append(name);
        builder.append(", url=");
        builder.append(url);
        builder.append(", color=");
        builder.append(color);
        builder.append(", jobs=");
        builder.append(jobs.toString());
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object other){
        boolean result = this == other;
        if (!result && other != null && other instanceof JenkinsJob) {
            final JenkinsJob obj = (JenkinsJob) other;
            result = url.equals(obj.getUrl());
        }
        return result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getClassAtt() {
        return classAtt;
    }

    public List<JenkinsJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }

    public void setClassAtt(String classAtt) {
        this.classAtt = classAtt;
    }
}