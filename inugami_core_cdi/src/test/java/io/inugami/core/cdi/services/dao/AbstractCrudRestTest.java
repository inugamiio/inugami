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
package io.inugami.core.cdi.services.dao;

import io.inugami.api.dao.DaoException;
import io.inugami.api.models.JsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * AbstractCrudRestTest
 *
 * @author patrick_guillerm
 * @since 11 janv. 2018
 */
public class AbstractCrudRestTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConvertFilters() throws Exception {
        final AbstractCrudRest crud = new TestAbstractCrudRest();

        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        json.addField("foo").valueQuot("bar").addSeparator();
        json.addField("hello").valueQuot("the world");
        json.closeObject();

        final Map<String, String> data = crud.convertFilters(json.toString());
        assertNotNull(data);
        assertEquals("bar", data.get("foo"));
        assertEquals("the world", data.get("hello"));
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    class TestAbstractCrudRest extends AbstractCrudRest<TestEntity, Long> {

        @Override
        protected Class<? extends TestEntity> initType() {
            return TestEntity.class;
        }

        @Override
        protected Long parseUid(final String uid) throws DaoException {
            return Long.parseLong(uid);
        }

        @Override
        protected TestEntity secureXssEntity(final TestEntity entity) {
            return entity;
        }

    }
}
