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
package io.inugami.framework.api.tools;

import io.inugami.framework.interfaces.models.JsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Temp {

    public static void main(String... args) throws IOException {

        String[] types = {"success", "error", "warning"};

        LocalDateTime date   = LocalDateTime.now().minusYears(1);
        final var     now    = LocalDateTime.now();
        JsonBuilder   json   = new JsonBuilder();
        FileWriter    writer = new FileWriter("/home/pguillerm/dev/workspaces/inugami/inugami/inugami_framework/inugami_api/target/tmp.json");
        long          i      = 0;
        writer.write("[");
        while (date.isBefore(now)) {
            i++;
            if (i % 100 == 0) {
                log.info("{}  / {}", date, now);
            }

            List<String> items = new ArrayList<>();
            for (String type : types) {
                if (Math.random() > 0.5) {
                    int max = 10;
                    if (type == "success") {
                        max = 30;
                    }

                    int value = (int) Math.floor(Math.random() * max);
                    value = value <= 1 ? 1 : value;
                    items.add("{type:\"" + type + "\",value:" + value + "}");
                }
            }

            date.plusMinutes(1);

            json.write("{time: \"" + date.format(DateTimeFormatter.ISO_DATE_TIME) + "\",items: [" +
                       String.join(",", items) + "]}");
            if (date.isBefore(now)) {
                json.addSeparator();
            }
            writer.write(json.toString());
            json.clear();
        }
        writer.write("]");
        writer.close();
        log.info("done");

    }
}
