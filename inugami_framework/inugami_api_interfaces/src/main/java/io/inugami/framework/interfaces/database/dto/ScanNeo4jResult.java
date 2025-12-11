package io.inugami.framework.interfaces.database.dto;

import lombok.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.processIfNotNull;


@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ScanNeo4jResult implements Serializable {
    private static final long               serialVersionUID = -1885576008301619055L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String             type;
    @Builder.Default
    private              List<String>       nodesToDeletes = new ArrayList<>();
    @ToString.Include
    @Builder.Default
    private              List<Node>         nodes = new ArrayList<>();
    @Builder.Default
    private              List<String>       createScripts= new ArrayList<>();
    @ToString.Include
    @Builder.Default
    private              List<Relationship> relationships= new ArrayList<>();
    @Builder.Default
    private              List<Relationship> relationshipsToDeletes= new ArrayList<>();
    @Builder.Default
    private              List<String>       deleteScripts= new ArrayList<>();


    public ScanNeo4jResult sort() {
        Collections.sort(nodesToDeletes);
        Collections.sort(nodes);
        Collections.sort(createScripts);
        Collections.sort(relationships);
        Collections.sort(relationshipsToDeletes);
        Collections.sort(deleteScripts);

        nodes.stream().forEach(Node::sort);
        relationships.stream().forEach(Relationship::sort);
        relationshipsToDeletes.stream().forEach(Relationship::sort);
        return this;
    }

    public ScanNeo4jResult addNode(final List<Node> values) {
        appendIfNotNull(values, nodes::addAll);
        return this;
    }


    public ScanNeo4jResult addNode(final Node... values) {
        appendIfNotNull(Arrays.asList(values), nodes::addAll);
        return this;
    }

    public ScanNeo4jResult addCreateScript(final List<String> values) {
        appendIfNotNull(values, createScripts::addAll);
        return this;
    }


    public ScanNeo4jResult addCreateScript(final String... values) {
        appendIfNotNull(Arrays.asList(values), createScripts::addAll);
        return this;
    }


    public ScanNeo4jResult addNodeToDelete(final List<String> uids) {
        processIfNotNull(uids, this.nodesToDeletes::addAll);
        return this;
    }

    public ScanNeo4jResult addNodeToDelete(final String... uids) {
        appendIfNotNull(Arrays.asList(uids), nodesToDeletes::addAll);
        return this;
    }


    public ScanNeo4jResult addRelationship(final List<Relationship> values) {
        processIfNotNull(values, this.relationships::addAll);
        return this;
    }

    public ScanNeo4jResult addRelationship(final Relationship... values) {
        processIfNotNull(Arrays.asList(values), this.relationships::addAll);
        return this;
    }


    public ScanNeo4jResult addRelationshipToDelete(final List<Relationship> values) {
        processIfNotNull(values, this.relationshipsToDeletes::addAll);
        return this;
    }

    public ScanNeo4jResult addRelationshipToDelete(final Relationship... values) {
        processIfNotNull(Arrays.asList(values), this.relationshipsToDeletes::addAll);
        return this;
    }


    public ScanNeo4jResult addDeleteScript(final List<String> values) {
        processIfNotNull(values, this.deleteScripts::addAll);
        return this;
    }

    public ScanNeo4jResult addDeleteScript(final String... values) {
        processIfNotNull(Arrays.asList(values), this.deleteScripts::addAll);
        return this;
    }

    private <T> void appendIfNotNull(final List<T> values, final Consumer<List<T>> consumer) {
        if (values != null && !values.isEmpty() && consumer!=null) {
            consumer.accept(values.stream().filter(Objects::nonNull).toList());
        }
    }

    public static void merge(final ScanNeo4jResult providerResult, final ScanNeo4jResult result) {
        if (providerResult != null && result != null) {
            //@formatter:off
            processIfNotNull(providerResult.getNodes(), values -> result.addNode(values));
            processIfNotNull(providerResult.getRelationships(), values -> result.addRelationship(values));
            processIfNotNull(providerResult.getNodesToDeletes(), values -> result.addNodeToDelete(values));
            processIfNotNull(providerResult.getRelationshipsToDeletes(), values -> result.addRelationshipToDelete(values));
            processIfNotNull(providerResult.getCreateScripts(), values -> result.addCreateScript(values));
            processIfNotNull(providerResult.getDeleteScripts(), values -> result.addDeleteScript(values));
            //@formatter:on
        }
    }
}
