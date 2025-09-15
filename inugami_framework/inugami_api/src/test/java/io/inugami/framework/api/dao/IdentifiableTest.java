package io.inugami.framework.api.dao;

import io.inugami.framework.interfaces.dao.Identifiable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifiableTest {


    @Test
    void uidFieldName_nominal() {
        assertThat(new EntityObj().uidFieldName()).isEqualTo("uid");
    }


    private static class EntityObj implements Identifiable<String> {

        @Override
        public String getUid() {
            return null;
        }

        @Override
        public void setUid(final String uid) {

        }

        @Override
        public boolean isUidSet() {
            return false;
        }
    }

}