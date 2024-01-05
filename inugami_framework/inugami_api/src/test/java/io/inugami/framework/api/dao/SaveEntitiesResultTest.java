package io.inugami.framework.api.dao;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.dao.SaveEntitiesResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SaveEntitiesResultTest {

    @Test
    void nominal() {
        SaveEntitiesResult<String> result = new SaveEntitiesResult<>();

        result.addNewEntity(null);
        result.addNewEntity("01");
        result.addAllNewEntities(null);
        result.addAllNewEntities(List.of("02"));
        result.addMergeEntity(null);
        result.addMergeEntity("03");
        result.addAllMergeEntities(null);
        result.addAllMergeEntities(List.of("04"));

        UnitTestHelper.assertTextRelative(result, "io/inugami/framework/api/saveEntitiesResultTest/nominal.json");
        UnitTestHelper.assertTextRelative(result.getNewEntities(), "io/inugami/framework/api/saveEntitiesResultTest/nominal.newEntities.json");
        UnitTestHelper.assertTextRelative(result.getMergeEntities(), "io/inugami/framework/api/saveEntitiesResultTest/nominal.mergeEntities.json");

        assertThat(result).hasToString("SaveEntitiesResult(newEntities=[01, 02], mergeEntities=[03, 04])");
    }
}