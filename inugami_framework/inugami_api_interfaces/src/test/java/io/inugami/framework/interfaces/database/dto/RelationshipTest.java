package io.inugami.framework.interfaces.database.dto;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import io.inugami.framework.interfaces.testing.commons.UnitTestData;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class RelationshipTest {
    @Test
    void relationship() {
        assertDto(AssertDtoContext.<Relationship>builder()
                                  .objectClass(Relationship.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(Relationship::new)
                                  .fullArgConstructor(this::buildRelationship)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/database/dto/relationship/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/database/dto/relationship/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/database/dto/relationship/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(Relationship instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().from(null).build());
        assertThat(instance.toBuilder().from(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().from(OTHER).build());
        assertThat(instance.toBuilder().from(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().from(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().from(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().to(null).build());
        assertThat(instance.toBuilder().to(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().to(OTHER).build());
        assertThat(instance.toBuilder().to(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().to(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().to(OTHER).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(null).build());
        assertThat(instance.toBuilder().type(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().type(OTHER).build());
        assertThat(instance.toBuilder().type(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().type(OTHER).build().hashCode());


    }
    @Test
    void compareTo_nominal() {
        assertThat(buildDataSet().compareTo(null)).isEqualTo(-1);
        assertThat(buildDataSet().compareTo(buildDataSet())).isEqualTo(0);
        assertThat(buildDataSet().compareTo(buildDataSet().toBuilder().to("aaa").build())).isEqualTo(1);
        assertThat(buildDataSet().compareTo(buildDataSet().toBuilder().to("zzz").build())).isEqualTo(-1);
    }

    @Test
    void buildHash_nominal() {
        assertThat(buildRelationship().buildHash()).isEqualTo("2940bf84-22ed-4dca-baa8-c10111847c9e-[USER_PLACE]->3aa93701-dbf6-4554-9148-fccf24354f30");
    }
    @Test
    void properties_nominal() {
        final Relationship result = Relationship.builder()
                                                .properties(Map.ofEntries(Map.entry("date", "2023-05-21")))
                                                .properties(Map.ofEntries(Map.entry("date", "2023-05-21")))
                                                .build();
        assertText(result,
                """
                        {
                          "properties" : {
                            "date" : "2023-05-21"
                          }
                        }
                """);
    }


    @Test
    void addProperty_nominal() {

        assertText(Relationship.builder()
                                       .property("date", "2023-05-21")
                                       .build(),
                   """
                           {
                             "properties" : {
                               "date" : "2023-05-21"
                             }
                           }
                   """);

        assertText(Relationship.builder()
                                       .property(null, "2023-05-21")
                                       .build(),
                   """
                           {
                             "properties" : { }
                           }
                   """);
        assertText(Relationship.builder()
                                       .property("date", null)
                                       .build(),
                   """
                           {
                             "properties" : { }
                           }
                   """);
    }

    private Relationship buildRelationship() {
        return Relationship.builder()
                           .from("2940bf84-22ed-4dca-baa8-c10111847c9e")
                           .to("3aa93701-dbf6-4554-9148-fccf24354f30")
                           .type("USER_PLACE")
                           .property("date", UnitTestData.DATE_TIME.toLocalDate())
                           .build()
                           .toBuilder()
                           .build();
    }
    public static Relationship buildDataSet() {
        return Relationship.builder()
                           .type("has_relationship")
                           .from("nodeA")
                           .to("nodeB")
                           .properties(null)
                           .properties(Map.ofEntries(Map.entry("date", "2023-05-21")))
                           .property(null, OTHER)
                           .property(OTHER, null)
                           .property("color", "red")
                           .build()
                           .toBuilder()
                           .build();
    }
}