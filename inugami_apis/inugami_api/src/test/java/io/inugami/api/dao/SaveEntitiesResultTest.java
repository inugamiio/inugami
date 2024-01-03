package io.inugami.api.dao;

import io.inugami.interfaces.dao.SaveEntitiesResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertTextRelative;
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

        assertTextRelative(result, "api/dao/saveEntitiesResultTest/nominal.json");
        assertTextRelative(result.getNewEntities(), "api/dao/saveEntitiesResultTest/nominal.newEntities.json");
        assertTextRelative(result.getMergeEntities(), "api/dao/saveEntitiesResultTest/nominal.mergeEntities.json");

        assertThat(result).hasToString("SaveEntitiesResult(newEntities=[01, 02], mergeEntities=[03, 04])");
    }
}