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
package io.inugami.monitoring.core.interceptors.mdc;


import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.MdcCleanerSPI;

public class DefaultMdcCleaner implements MdcCleanerSPI {
    @Override
    public void clean() {
        MdcService.getInstance().clear();
    }
}
