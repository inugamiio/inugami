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
package io.inugami.interfaces.models.event;


import io.inugami.interfaces.models.ClonableObject;
import io.inugami.interfaces.processors.ProcessorModel;

import java.io.Serializable;
import java.util.List;

/**
 * GenericEvent
 *
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
public interface GenericEvent<T> extends Serializable, ClonableObject<T> {

    String getName();

    void setName(final String name);

    String getFromFirstTime();

    void setFromFirstTime(final String fromFirstTime);

    String getFrom();

    void setFrom(final String from);

    String getUntil();

    void setUntil(final String until);

    String getProvider();

    void setProvider(final String provider);

    String getMapper();

    void setMapper(final String mapper);

    List<ProcessorModel> getProcessors();

    void setProcessors(final List<ProcessorModel> processors);

    List<AlertingModel> getAlertings();

    void setAlertings(final List<AlertingModel> alertings);
}
