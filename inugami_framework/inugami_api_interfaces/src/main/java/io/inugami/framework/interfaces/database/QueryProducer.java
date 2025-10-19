package io.inugami.framework.interfaces.database;



import io.inugami.framework.interfaces.database.dto.QueryDefinition;

import java.util.List;

@FunctionalInterface
public interface QueryProducer {
    List<QueryDefinition> extractQueries();
}
