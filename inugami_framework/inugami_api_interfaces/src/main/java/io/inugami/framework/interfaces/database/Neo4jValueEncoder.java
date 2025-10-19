package io.inugami.framework.interfaces.database;

@FunctionalInterface
public interface Neo4jValueEncoder {
    String encode(Object value);
}