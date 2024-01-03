package io.inugami.api.models;

import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.interfaces.models.maven.Gav;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class GavTest {


    @Test
    void gav() {
        assertDto(new AssertDtoContext<Gav>().toBuilder()
                                             .objectClass(Gav.class)
                                             .fullArgConstructorRefPath("api/models/gav/model.json")
                                             .getterRefPath("api/models/gav/getter.json")
                                             .toStringRefPath("api/models/gav/toString.txt")
                                             .cloneFunction(instance -> instance.toBuilder().build())
                                             .noArgConstructor(Gav::new)
                                             .fullArgConstructor(this::buildDataSet)
                                             .noEqualsFunction(this::notEquals)
                                             .checkSetters(false)
                                             .checkSerialization(false)
                                             .build());
    }

    @Test
    void getHash_nominal() {
        assertThat(buildDataSet().getHash()).hasToString("io.inugami:inugami_api:3.3.0:jar");
    }

    @Test
    void addHash_nominal() {
        assertThat(Gav.builder()
                      .addHash("io.inugami:inugami_api:3.3.0:jar")
                      .build()).hasToString("Gav(groupId=io.inugami, artifactId=inugami_api, version=3.3.0, qualifier=jar)");

        assertThat(Gav.builder()
                      .addHash("io.inugami:inugami_api:3.3.0")
                      .build()).hasToString("Gav(groupId=io.inugami, artifactId=inugami_api, version=3.3.0, qualifier=null)");
        assertThat(Gav.builder()
                      .addHash("io.inugami:inugami_api:3.3.0")
                      .build()
                      .getHash()).hasToString("io.inugami:inugami_api:3.3.0");

        assertThat(Gav.builder()
                      .addHash("io.inugami:inugami_api")
                      .build()).hasToString("Gav(groupId=io.inugami, artifactId=inugami_api, version=null, qualifier=null)");

        assertThat(Gav.builder()
                      .addHash("io.inugami")
                      .build()).hasToString("Gav(groupId=io.inugami, artifactId=null, version=null, qualifier=null)");

        assertThat(Gav.builder()
                      .addHash(null)
                      .build()).hasToString("Gav(groupId=null, artifactId=null, version=null, qualifier=null)");
    }

    @Test
    void equalsWithoutVersion_nominal() {
        assertThat(buildDataSet().equalsWithoutVersion(buildDataSet().toBuilder().version("3.3.3").build())).isTrue();

        assertThat(buildDataSet().equalsWithoutVersion(null)).isFalse();
        assertThat(buildDataSet().equalsWithoutVersion(buildDataSet().toBuilder().version("3.3.3"))).isFalse();
        assertThat(buildDataSet().equalsWithoutVersion(buildDataSet().toBuilder()
                                                                     .groupId(OTHER)
                                                                     .version("3.3.3")
                                                                     .build())).isFalse();
    }

    void notEquals(final Gav instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().groupId(null).build());
        assertThat(instance.toBuilder().groupId(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().groupId(OTHER).build());
        assertThat(instance.toBuilder().groupId(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().groupId(null).build().hashCode());
        assertThat(instance.toBuilder().groupId(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().artifactId(null).build());
        assertThat(instance.toBuilder().artifactId(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().artifactId(OTHER).build());
        assertThat(instance.toBuilder().artifactId(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().artifactId(null).build().hashCode());
        assertThat(instance.toBuilder().artifactId(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().version(null).build());
        assertThat(instance.toBuilder().version(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().version(OTHER).build());
        assertThat(instance.toBuilder().version(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().version(null).build().hashCode());
        assertThat(instance.toBuilder().version(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().qualifier(null).build());
        assertThat(instance.toBuilder().qualifier(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().qualifier(OTHER).build());
        assertThat(instance.toBuilder().qualifier(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().qualifier(null).build().hashCode());
        assertThat(instance.toBuilder().qualifier(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private Gav buildDataSet() {
        return Gav.builder()
                  .groupId("io.inugami")
                  .artifactId("inugami_api")
                  .version("3.3.0")
                  .qualifier("jar")
                  .build();
    }

}