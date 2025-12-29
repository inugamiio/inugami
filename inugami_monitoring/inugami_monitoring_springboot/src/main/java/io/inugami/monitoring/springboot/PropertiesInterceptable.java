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
package io.inugami.monitoring.springboot;

import io.inugami.framework.interfaces.monitoring.Interceptable;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@Component
public class PropertiesInterceptable implements Interceptable {
    @Value("${inugami.monitoring.interceptor.url.skip:#{null}}")
    @Setter(AccessLevel.PACKAGE)
    private String skipUrl;

    private List<Pattern> patterns = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (skipUrl != null) {
            final String[] parts = skipUrl.split(";");
            for (String part : parts) {
                patterns.add(Pattern.compile(part));
            }
        }
    }

    @Override
    public boolean isInterceptable(final RequestData request) {
        if (patterns.isEmpty()) {
            return true;
        }
        final String path = Optional.ofNullable(request.getUri()).orElse("");
        for (Pattern pattern : patterns) {
            if (pattern.matcher(path).matches()) {
                return false;
            }
        }
        return true;
    }
}
