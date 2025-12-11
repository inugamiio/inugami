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
package io.inugami.dashboard.webapp.runner;

import io.inugami.dashboard.webapp.InugamiDashboardApplication;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;

import java.io.File;
import java.io.IOException;

public class InugamiDashboardApplicationRunner {

    public static void main(String[] args) {
        String workspace = null;
        try {
            workspace = new File(".").getCanonicalFile().toString()
                        + File.separator + "src"
                        + File.separator + "test"
                        + File.separator + "resources"
                        + File.separator + "workspace";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.setProperty(JvmKeyValues.JVM_HOME_PATH.getKey(), workspace);
        InugamiDashboardApplication.main(args);


    }

}
