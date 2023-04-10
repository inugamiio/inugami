package io.inugami.commons.test;

import io.inugami.commons.test.dto.UserDto;

public interface UnitTestHelperStubTestDAO {

    UserDto getUserById(final long id);

    UserDto search(final String name);
}
