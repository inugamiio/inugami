package org.inugami.core.providers.jenkins.models;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.basic.JsonObject;

import flexjson.JSONDeserializer;
import flexjson.JSONException;

public class JenkinsInformation implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2672242606126191964L;
    private List<JenkinsJob> jobs;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public JenkinsInformation() {}

    public JenkinsInformation(List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public <T extends JsonObject> T convertToObject(byte[] data, Charset charset) {
        JenkinsInformation result = null;

        if (data != null) {
            String json = charset == null ? new String(data).trim() : new String(data, charset).trim();

            if (!json.isEmpty()) {
                try {
                    result = new JSONDeserializer<JenkinsInformation>()
                        .deserialize(json, JenkinsInformation.class);

                } catch (JSONException error) {
                    Loggers.DEBUG.error("{} : \n payload:\n----------\n{}\n----------\n", error.getMessage(),
                            json);
                    Loggers.PROVIDER.error(error.getMessage());
                }
            }
        }

        return  (T) result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public List<JenkinsJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }

    @Override
    public JsonObject cloneObj() {
        final List<JenkinsJob> newJenkinsJob = new ArrayList<>();
        if (jobs != null) {
            for (JenkinsJob item : jobs) {
                newJenkinsJob.add(new JenkinsJob(item.getClassAtt(), item.getName(), item.getUrl(), item.getColor()));
            }
        }
        return new JenkinsInformation(newJenkinsJob);
    }
}