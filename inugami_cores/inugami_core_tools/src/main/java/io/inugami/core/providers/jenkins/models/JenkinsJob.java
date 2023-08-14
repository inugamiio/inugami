package io.inugami.core.providers.jenkins.models;

import flexjson.JSON;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class JenkinsJob implements Serializable {

    private static final long serialVersionUID = -262058079809073888L;

    @JSON(name = "_class")
    private String classAtt;

    private String name;
    @EqualsAndHashCode.Include
    private String url;
    private String color;

    private List<JenkinsJob> jobs;

}