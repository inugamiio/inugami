package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import io.inugami.framework.interfaces.testing.commons.UnitTestData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class NodeTest {
    @Test
    void node() {
        assertDto(AssertDtoContext.<Node>builder()
                                  .objectClass(Node.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Node::new)
                                  .fullArgConstructor(this::buildNode)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/node/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/node/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/node/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    @Test
    void compareTo_nominal() {
        final List<Node> nodes = new ArrayList<>(List.of(
                buildNode(),
                Node.builder()
                    .type("user")
                    .name("User2")
                    .uid("e37de9b4-9349-401a-aa0b-fe66f27a4242")
                    .addProperty("level", 100)
                    .addProperty("lastname", UnitTestData.LASTNAME)
                    .addProperty("firstname", UnitTestData.FIRSTNAME)
                    .build(),
                Node.builder()
                    .type("node")
                    .name("MyNode2")
                    .uid("83d2701c-ffe4-4248-83e9-90b6e591ac60")
                    .addProperty("size", 5)
                    .build()
        ));
        Collections.sort(nodes);
        nodes.forEach(Node::sort);
        assertText(nodes,
                   """
                           [ {
                             "name" : "MyNode2",
                             "properties" : {
                               "size" : 5
                             },
                             "type" : "node",
                             "uid" : "83d2701c-ffe4-4248-83e9-90b6e591ac60"
                           }, {
                             "name" : "MyNode",
                             "properties" : {
                               "size" : 15
                             },
                             "type" : "node",
                             "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                           }, {
                             "name" : "User2",
                             "properties" : {
                               "firstname" : "John",
                               "lastname" : "Smith",
                               "level" : 100
                             },
                             "type" : "user",
                             "uid" : "e37de9b4-9349-401a-aa0b-fe66f27a4242"
                           } ]
                           """);
    }


    private void notEquals(Node instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(null).build());
        assertThat(instance.toBuilder().type(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(OTHER).build());
        assertThat(instance.toBuilder().type(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().uid(null).build());
        assertThat(instance.toBuilder().uid(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().uid(OTHER).build());
        assertThat(instance.toBuilder().uid(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().uid(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().uid(OTHER).build().hashCode());
    }

    private Node buildNode() {
        return Node.builder()
                   .type("node")
                   .name("MyNode")
                   .uid(UnitTestData.UID)
                   .addProperty("size", 15)
                   .build()
                   .toBuilder()
                   .build();
    }
}