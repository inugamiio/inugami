package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import io.inugami.framework.interfaces.testing.commons.UnitTestData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class ScanNeo4jResultTest {
    @Test
    void scanNeo4jResult() {
        assertDto(AssertDtoContext.<ScanNeo4jResult>builder()
                                  .objectClass(ScanNeo4jResult.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(ScanNeo4jResult::new)
                                  .fullArgConstructor(this::buildScanNeo4jResult)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/scanNeo4jResult/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/scanNeo4jResult/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/scanNeo4jResult/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(ScanNeo4jResult instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(null).build());
        assertThat(instance.toBuilder().type(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(OTHER).build());
        assertThat(instance.toBuilder().type(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(OTHER).build().hashCode());
    }

    @Test
    void addNode_nominal() {
        final ScanNeo4jResult result    = buildDataSet();
        List<Node>            nullValue = null;
        result.addNode(nullValue);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);

        result.addNode(List.of(Node.builder().name("simpleNode").build()));
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             }, {
                               "name" : "simpleNode"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
    }

    @Test
    void addCreateScript_nominal() {
        final ScanNeo4jResult result    = buildDataSet();
        List<String>          nullValue = null;
        result.addCreateScript(nullValue);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        result.addCreateScript(List.of("requests"));
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script", "requests" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
    }


    @Test
    void addNodeToDelete_nominal() {
        final ScanNeo4jResult result    = buildDataSet();
        List<String>          nullValue = null;
        result.addNodeToDelete(nullValue);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        result.addNodeToDelete(List.of("requests"));
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD", "requests" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        result.addNodeToDelete("uid");
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD", "requests", "uid" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
    }

    @Test
    void addRelationship_nominal() {
        final ScanNeo4jResult result    = buildDataSet();
        List<Relationship>    nullValue = null;
        result.addRelationship(nullValue);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        result.addRelationship(List.of(Relationship.builder().from("1").to("2").type("has").build()));
        assertText(result,
                """
                {
                  "createScripts" : [ "create script", "other create script" ],
                  "deleteScripts" : [ "deleteScripts", "other delete script" ],
                  "nodes" : [ {
                    "name" : "artifact",
                    "properties" : {
                      "color" : "blue",
                      "date" : "2023-05-21"
                    },
                    "type" : "Version",
                    "uid" : "uid"
                  }, {
                    "name" : "artifact",
                    "properties" : {
                      "color" : "blue",
                      "date" : "2023-05-21"
                    },
                    "type" : "addNode",
                    "uid" : "uid"
                  } ],
                  "nodesToDeletes" : [ "nodeD" ],
                  "relationships" : [ {
                    "from" : "nodeA",
                    "properties" : {
                      "color" : "red",
                      "date" : "2023-05-21"
                    },
                    "to" : "nodeB",
                    "type" : "has_relationship"
                  }, {
                    "from" : "nodeA",
                    "properties" : {
                      "color" : "red",
                      "date" : "2023-05-21"
                    },
                    "to" : "nodeB",
                    "type" : "other_relationship"
                  }, {
                    "from" : "1",
                    "to" : "2",
                    "type" : "has"
                  } ],
                  "relationshipsToDeletes" : [ {
                    "from" : "nodeA",
                    "properties" : {
                      "color" : "red",
                      "date" : "2023-05-21"
                    },
                    "to" : "nodeB",
                    "type" : "to_delete"
                  } ],
                  "type" : "neo4j"
                }
                """);
    }

    @Test
    void addRelationshipToDelete_nominal() {
        final ScanNeo4jResult result    = buildDataSet();
        List<Relationship>    nullValue = null;
        result.addRelationshipToDelete(nullValue);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        result.addRelationshipToDelete(List.of(Relationship.builder().from("1").to("2").type("has").build()));
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             }, {
                               "from" : "1",
                               "to" : "2",
                               "type" : "has"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        result.addRelationshipToDelete(Relationship.builder().from("3").to("4").type("has").build());
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             }, {
                               "from" : "1",
                               "to" : "2",
                               "type" : "has"
                             }, {
                               "from" : "3",
                               "to" : "4",
                               "type" : "has"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
    }


    @Test
    void merge_nominal() {
        final ScanNeo4jResult result = buildDataSet();
        ScanNeo4jResult.merge(result, null);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        ScanNeo4jResult.merge(null, result);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        ScanNeo4jResult.merge(result, ScanNeo4jResult.builder().build());
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
        ScanNeo4jResult.merge(result, result);
        assertText(result,
                   """
                           {
                             "createScripts" : [ "create script", "other create script", "create script", "other create script" ],
                             "deleteScripts" : [ "deleteScripts", "other delete script", "deleteScripts", "other delete script" ],
                             "nodes" : [ {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "Version",
                               "uid" : "uid"
                             }, {
                               "name" : "artifact",
                               "properties" : {
                                 "color" : "blue",
                                 "date" : "2023-05-21"
                               },
                               "type" : "addNode",
                               "uid" : "uid"
                             } ],
                             "nodesToDeletes" : [ "nodeD", "nodeD" ],
                             "relationships" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "has_relationship"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "other_relationship"
                             } ],
                             "relationshipsToDeletes" : [ {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             }, {
                               "from" : "nodeA",
                               "properties" : {
                                 "color" : "red",
                                 "date" : "2023-05-21"
                               },
                               "to" : "nodeB",
                               "type" : "to_delete"
                             } ],
                             "type" : "neo4j"
                           }
                           """);
    }

    private ScanNeo4jResult buildScanNeo4jResult() {
        return ScanNeo4jResult.builder()
                              .type("USER")
                              .nodesToDeletes(List.of("a3a7c61d-8ed3-4f33-9d26-aff3dc3a5631"))
                              .nodes(List.of(buildNode()))
                              .createScripts(List.of("script"))
                              .relationships(List.of(buildRelationship()))
                              .relationshipsToDeletes(List.of(buildRelationship()))
                              .deleteScripts(List.of("script"))
                              .build()
                              .toBuilder()
                              .build();

    }

    private Node buildNode() {
        return Node.builder()
                   .type("node")
                   .name("MyNode")
                   .uid(UnitTestData.UID)
                   .addProperty("size", 15)
                   .build();
    }

    private Relationship buildRelationship() {
        return Relationship.builder()
                           .from("2940bf84-22ed-4dca-baa8-c10111847c9e")
                           .to("3aa93701-dbf6-4554-9148-fccf24354f30")
                           .type("USER_PLACE")
                           .property("date", UnitTestData.DATE_TIME.toLocalDate())
                           .build();
    }

    public static ScanNeo4jResult buildDataSet() {
        return ScanNeo4jResult.builder()
                              .type("neo4j")
                              .nodesToDeletes(new ArrayList<>(List.of("nodeD")))
                              .nodes(new ArrayList<>(List.of(buildNodeData())))
                              .createScripts(new ArrayList<>(List.of("create script")))
                              .relationships(new ArrayList<>(List.of(RelationshipTest.buildDataSet())))
                              .relationshipsToDeletes(new ArrayList<>(List.of(RelationshipTest.buildDataSet()
                                                                                              .toBuilder()
                                                                                              .type("to_delete")
                                                                                              .build())))
                              .deleteScripts(new ArrayList<>(List.of("deleteScripts")))
                              .build()
                              .sort()
                              .addNode(buildNodeData().toBuilder().type("addNode").build())
                              .addRelationship(RelationshipTest.buildDataSet()
                                                               .toBuilder()
                                                               .type("other_relationship")
                                                               .build())
                              .addCreateScript("other create script")
                              .addDeleteScript("other delete script");
    }

    public static Node buildNodeData() {
        return Node.builder()
                   .type("Version")
                   .name("artifact")
                   .uid("uid")
                   .properties(Map.ofEntries(Map.entry("date", "2023-05-21")))
                   .addProperty("color", "blue")
                   .build();
    }
}