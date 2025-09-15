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
package io.inugami.monitoring.core.spi;


import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodDTO;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;

public class DefaultJavaRestMethodTracker implements JavaRestMethodTracker {

    public static final String APP_CLASS = "app_class";
    public static final String APP_CLASS_SHORT_NAME = "app_class_shortName";
    public static final String APP_METHOD = "app_method";

    @Override
    public void track(final JavaRestMethodDTO data) {
        final MdcService mdc = MdcService.getInstance();
        mdc.appClass(data.getRestClass().getName());
        mdc.appClassShortName(data.getRestClass().getSimpleName());
        mdc.appMethod(data.getRestMethod().getName());
    }
}
