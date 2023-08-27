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
package io.inugami.commons.test;

import io.inugami.api.models.data.DataGeneratorUtils;
import io.inugami.commons.test.dto.UserDataDTO;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@SuppressWarnings({"java:S2386"})
@UtilityClass
public class UnitTestData {

    // =========================================================================
    // DATA
    // =========================================================================
    public static final long          ID                = 1L;
    public static final long          ID_OTHER          = 2L;
    public static final String        OTHER             = "XXX";
    public static final String[]      LOREM             = DataGeneratorUtils.LOREM;
    public static final String[]      CATEGORY          = DataGeneratorUtils.CATEGORY;
    public static final LocalDate     TODAY             = LocalDate.now();
    public static final LocalDateTime NOW_UTC           = LocalDateTime.now(ZoneOffset.UTC);
    public static final LocalDateTime DATE_TIME         = LocalDateTime.of(2023, 6, 1, 12, 0, 0);
    public static final String        UID               = "bb895294-efe7-484b-b670-14d004eaf461";
    public static final String        UID_CONTRACTED    = "bb895294efe7484bb67014d004eaf461";
    public static final String        DEVICE_IDENTIFIER = "4547abf0-4bd2-4ff7-ac6c-4eb27cd83c10";
    public static final String        CORRELATION_ID    = "57f5a259-87e0-41c9-b56e-0332dcd81887";
    public static final String        CONVERSATION_ID   = "90b79730-7276-402b-9fe2-be1543f484e4";
    public static final String        TRACE_ID          = "edff7c9651a5";
    public static final String        EMAIL             = "john.smith@mock.org";
    public static final String        FIRSTNAME         = "John";
    public static final String        LASTNAME          = "Smith";
    public static final String        LASTNAME2         = "Lalonde";
    public static final String        LOREM_IPSUM       = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
    public static final String        LOREM_IPSUM_2     = "Suspendisse in quam in felis mollis ullamcorper ac in lectus";
    public static final String        LOREM_IPSUM_3     = "Nulla non leo vitae nunc aliquam mollis";
    public static final String        LOREM_IPSUM_4     = "Duis fringilla facilisis dui sed luctus.";
    public static final String        NAVS13_1          = "7564971247732";
    public static final String        NAVS13_2          = "7560769504407";
    public static final String        NAVS13_3          = "7566987985080";
    public static final String        PHONE_NUMBER_1    = "0615031522";
    public static final String        PHONE_NUMBER_2    = "0225993475";
    public static final String        PHONE_NUMBER_3    = "0624455065";

    public static final String      STREET_NAME   = "du Château";
    public static final String      STREET_TYPE   = "Chem.";
    public static final String      CITY          = "Cheseaux-sur-Lausanne";
    public static final String      ZIP_CODE      = "1033";
    public static final String      STREET_NUMBER = "10";
    public static final String      CANTON        = "VD";
    public static final String      NATIONALITY   = "CH";
    // =========================================================================
    // USERS
    // =========================================================================
    public static final UserDataDTO USER_1        = UserDataDTO.builder()
                                                               .id(1L)
                                                               .sex(UserDataDTO.Sex.FEMALE)
                                                               .firstName("Émilie")
                                                               .lastName(LASTNAME2)
                                                               .email("emilie.lalonde@mock.org")
                                                               .phoneNumber(PHONE_NUMBER_1)
                                                               .old(35)
                                                               .birthday(LocalDate.of(1988, 4, 12))
                                                               .socialId(NAVS13_1)
                                                               .nationality(NATIONALITY)
                                                               .canton(CANTON)
                                                               .streetName(STREET_NAME)
                                                               .streetNumber(STREET_NUMBER)
                                                               .streetType(STREET_TYPE)
                                                               .zipCode(ZIP_CODE)
                                                               .city(CITY)
                                                               .deviceIdentifier("401f0498-c43f-43ad-a3f4-2888838332ad")
                                                               .build();


    public static final UserDataDTO USER_2 = UserDataDTO.builder()
                                                        .id(2L)
                                                        .sex(UserDataDTO.Sex.FEMALE)
                                                        .firstName("Jessamine")
                                                        .lastName(LASTNAME2)
                                                        .email("jessamine.lalonde@mock.org")
                                                        .phoneNumber(PHONE_NUMBER_2)
                                                        .old(10)
                                                        .birthday(LocalDate.of(2013, 6, 21))
                                                        .socialId(NAVS13_2)
                                                        .nationality(NATIONALITY)
                                                        .canton(CANTON)
                                                        .streetName(STREET_NAME)
                                                        .streetNumber(STREET_NUMBER)
                                                        .streetType(STREET_TYPE)
                                                        .zipCode(ZIP_CODE)
                                                        .city(CITY)
                                                        .deviceIdentifier("46bfaa1a-0adf-4660-994a-e56f4b059c8f")
                                                        .build();


    public static final UserDataDTO USER_3 = UserDataDTO.builder()
                                                        .id(3L)
                                                        .sex(UserDataDTO.Sex.MALE)
                                                        .firstName("Théodore")
                                                        .lastName(LASTNAME2)
                                                        .email("theodore.lalonde@mock.org")
                                                        .phoneNumber(PHONE_NUMBER_3)
                                                        .old(38)
                                                        .birthday(LocalDate.of(1985, 7, 10))
                                                        .socialId(NAVS13_3)
                                                        .nationality(NATIONALITY)
                                                        .canton(CANTON)
                                                        .streetName(STREET_NAME)
                                                        .streetNumber(STREET_NUMBER)
                                                        .streetType(STREET_TYPE)
                                                        .zipCode(ZIP_CODE)
                                                        .city(CITY)
                                                        .deviceIdentifier("45fa3dd1-714e-4887-b27d-9792b327ad56")
                                                        .build();

    // =========================================================================
    // RANDOM
    // =========================================================================
    static String getRandomUid() {
        return UUID.randomUUID().toString();
    }

    static String getRandomWord() {
        return DataGeneratorUtils.getWord();
    }

    static String getRandomCategory() {
        return DataGeneratorUtils.getCategory();
    }

    static String getRandomLabel() {
        return DataGeneratorUtils.getLabel();
    }

    static String getRandomPhrase() {
        return DataGeneratorUtils.getPhrase();
    }

    static String getRandomPhrase(final int nbWordMin, final int nbWordMax, final boolean isLabel) {
        return DataGeneratorUtils.getPhrase(nbWordMin, nbWordMax, isLabel);
    }

    static String getRandomSection() {
        return DataGeneratorUtils.getSection();
    }

    static String getRandomSection(final int nbLine, final int nbWordMin, final int nbWordMax) {
        return DataGeneratorUtils.getSection(nbLine, nbWordMin, nbWordMax);
    }

    static double getRandomDouble(final double min, final double max) {
        return DataGeneratorUtils.getDouble(min, max);
    }

    static int getRandomBetween(final int start, final int end) {
        return DataGeneratorUtils.getRandomBetween(start, end);
    }
}
