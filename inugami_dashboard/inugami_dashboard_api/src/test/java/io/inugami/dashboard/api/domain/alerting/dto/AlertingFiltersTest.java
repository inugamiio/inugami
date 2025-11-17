/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.dashboard.api.domain.alerting.dto;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class AlertingFiltersTest {

    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(AlertingFilters.class);
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