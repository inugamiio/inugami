package io.inugami.dashboard.api.domain.alerting.dto;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class AlertingFiltersTest {

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClass(AlertingFilters.class);
    }


    @Test
    void assertFilters() {
        assertText(AlertingFilters.FILTERS,
                   """
                           [ {
                             "field" : "uid",
                             "matchType" : "IN"
                           }, {
                             "field" : "name",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "description",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "provider",
                             "matchType" : "IN"
                           }, {
                             "field" : "message",
                             "matchType" : "CONTAINS"
                           }, {
                             "field" : "level",
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