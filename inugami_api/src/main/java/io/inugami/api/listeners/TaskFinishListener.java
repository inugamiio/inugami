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
package io.inugami.api.listeners;

/**
 * Inugami heavily use multi-threading principle. To handle when a task is completed,
 * you can create listeners. To designed this listener we have the
 * <strong>TaskFinishListener</strong> interface.
 * This interface can be used in common use cases.
 * 
 * @author patrick_guillerm
 * @since 16 janv. 2017
 */
@FunctionalInterface
public interface TaskFinishListener {
    void onFinish(final long time, final long delais, final String name, final Object result, final Exception error);
}
