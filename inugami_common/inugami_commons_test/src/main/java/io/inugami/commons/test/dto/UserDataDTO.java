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
package io.inugami.commons.test.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class UserDataDTO implements Serializable {
    @ToString.Include
    private Long      id;
    @ToString.Include
    private String    firstName;
    @ToString.Include
    private String    lastName;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String    email;
    private Sex       sex;
    private String    phoneNumber;
    private int       old;
    private LocalDate birthday;
    private String    socialId;
    private String    nationality;

    private String streetNumber;
    private String streetName;
    private String streetType;
    private String zipCode;
    private String city;
    private String district;
    private String department;
    private String canton;
    private String region;
    private String state;
    private String country;

    private String deviceIdentifier;

    public enum Sex {
        MALE,
        FEMALE,
        OTHER
    }
}
