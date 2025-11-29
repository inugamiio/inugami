package io.inugami.framework.interfaces.tools;

import io.inugami.framework.interfaces.monitoring.logger.ConsoleColors;
import io.inugami.framework.interfaces.testing.commons.UnitTestData;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static io.inugami.framework.interfaces.tools.ListUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class ListUtilsTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final List<String> VALUES = buildValues();

    private static List<String> buildValues() {
        final List<String> result = new ArrayList<>();
        for (int i = 23; i >= 0; i--) {
            result.add(String.valueOf(i));
        }
        return result;
    }

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(ListUtils.class);
    }

    // =================================================================================================================
    // TO LIST
    // =================================================================================================================
    @Test
    void toList_withLong() {
        assertText(toList(1L, 2L, 3L),
                   """
                           [ 1, 2, 3 ]
                           """);
    }

    @Test
    void toList_withInt() {
        assertText(toList(1, 2, 3),
                   """
                           [ 1, 2, 3 ]
                           """);
    }

    @Test
    void toList_withConvertor() {
        assertText(toList(i -> String.valueOf(i), 1, 2, 3),
                   """
                           [ "1", "2", "3" ]
                           """);
    }

    @Test
    void toList_withObject() {
        assertText(toList(UnitTestData.USER_1, UnitTestData.USER_2),
                   """
                           [ {
                             "birthday" : "1988-04-12",
                             "canton" : "VD",
                             "city" : "Cheseaux-sur-Lausanne",
                             "deviceIdentifier" : "401f0498-c43f-43ad-a3f4-2888838332ad",
                             "email" : "emilie.lalonde@mock.org",
                             "firstName" : "Émilie",
                             "id" : 1,
                             "lastName" : "Lalonde",
                             "nationality" : "CH",
                             "old" : 35,
                             "phoneNumber" : "0615031522",
                             "sex" : "FEMALE",
                             "socialId" : "7564971247732",
                             "streetName" : "du Château",
                             "streetNumber" : "10",
                             "streetType" : "Chem.",
                             "zipCode" : "1033"
                           }, {
                             "birthday" : "2013-06-21",
                             "canton" : "VD",
                             "city" : "Cheseaux-sur-Lausanne",
                             "deviceIdentifier" : "46bfaa1a-0adf-4660-994a-e56f4b059c8f",
                             "email" : "jessamine.lalonde@mock.org",
                             "firstName" : "Jessamine",
                             "id" : 2,
                             "lastName" : "Lalonde",
                             "nationality" : "CH",
                             "old" : 10,
                             "phoneNumber" : "0225993475",
                             "sex" : "FEMALE",
                             "socialId" : "7560769504407",
                             "streetName" : "du Château",
                             "streetNumber" : "10",
                             "streetType" : "Chem.",
                             "zipCode" : "1033"
                           } ]
                           """);
    }


    // =================================================================================================================
    // BUCKETS
    // =================================================================================================================
    @Test
    void split_nominal() {
        assertText(split(VALUES),
                   """
                           [ [ "23", "22", "21", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0" ] ]
                           """);

        assertText(split(VALUES, 7),
                   """
                           [ [ "23", "22", "21", "20", "19", "18", "17" ], [ "16", "15", "14", "13", "12", "11", "10" ], [ "9", "8", "7", "6", "5", "4", "3" ], [ "2", "1", "0" ] ]
                           """);
    }

    @Test
    void processOverBucket_nominal() {
        final List<Collection<String>> bucketWithoutSize = new ArrayList<>();
        processOverBucket(VALUES, bucketWithoutSize::add);
        assertText(bucketWithoutSize,
                   """
                           [ [ "23", "22", "21", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0" ] ]
                           """);

        final List<Collection<String>> buckets = new ArrayList<>();
        processOverBucket(VALUES, 7, buckets::add);
        assertText(buckets,
                   """
                           [ [ "23", "22", "21", "20", "19", "18", "17" ], [ "16", "15", "14", "13", "12", "11", "10" ], [ "9", "8", "7", "6", "5", "4", "3" ], [ "2", "1", "0" ] ]
                           """);
    }

    @Test
    void toSet_nominal() {
        final var values = toSet(1, 2, 3, 4);
        assertThat(values).isInstanceOf(Set.class);
        assertText(values,
                   """
                           [ 1, 2, 3, 4 ]
                           """);
    }

    @Test
    void isEmpty_nominal() {
        assertThat(isEmpty(null)).isTrue();
        assertThat(isEmpty(List.of())).isTrue();

        assertThat(isEmpty(List.of(1))).isFalse();
    }

    @Test
    void isNotEmpty_nominal() {
        assertThat(isNotEmpty(null)).isFalse();
        assertThat(isNotEmpty(List.of())).isFalse();

        assertThat(isNotEmpty(List.of(1))).isTrue();
    }
}