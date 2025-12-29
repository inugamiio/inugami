package ${package}.api.domain.user.dto;
import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class UserFiltersTest {
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(UserFilters.class);
    }


    @Test
    void assertFilters() {
        assertText(UserFilters.FILTERS,
                   """
                           [ {
                             "field" : "uid",
                             "matchType" : "IN"
                           }, {
                             "field" : "firstName",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "lastName",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "email",
                             "matchType" : "IN"
                           }, {
                             "field" : "createdBy",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "createdDate",
                             "matchType" : "BETWEEN"
                           }, {
                             "field" : "lastModifiedBy",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "lastModifiedDate",
                             "matchType" : "BETWEEN"
                           }, {
                             "field" : "version",
                             "matchType" : "BETWEEN"
                           } ]
                           """);
    }
}