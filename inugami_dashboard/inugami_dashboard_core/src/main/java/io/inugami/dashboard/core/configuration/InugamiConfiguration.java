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
package io.inugami.dashboard.core.configuration;

import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties
public class InugamiConfiguration {
    public static final String USER_HOME = "user.home";

    @Builder.Default
    private InugamiConfigurationApplication application = InugamiConfigurationApplication.builder().build();
    @Builder.Default
    private InugamiConfigurationEngine      engine      = InugamiConfigurationEngine.builder().build();


    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InugamiConfigurationApplication {

        @Builder.Default
        private String name      = "inugami";
        @Builder.Default
        private String workspace = getWorkspacePath();

        private static String getWorkspacePath() {
            String folder = JvmKeyValues.JVM_HOME_PATH.get();
            if (folder == null) {
                folder = System.getenv(JvmKeyValues.JVM_HOME_PATH.getKey());
            }
            if (folder == null) {
                folder = System.getProperty(USER_HOME) + File.separator + ".inugami";
            }
            final File folderFile = new File(folder);
            try {
                return folderFile.getCanonicalPath().toString();
            } catch (IOException e) {
                return null;
            }
        }
    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InugamiConfigurationEngine {
        public static final int DEFAULT_MAX_THREAD = 50;
        public static final long DEFAULT_TIMEOUT = 60000L;
        @Builder.Default
        private int             maxThreads         = 20;
        @Builder.Default
        private long            timeout            = DEFAULT_TIMEOUT;
    }
}
