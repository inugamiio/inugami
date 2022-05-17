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
package io.inugami.api.monitoring.warn;

import io.inugami.api.exceptions.MessagesFormatter;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Getter
public class DefaultWarn implements WarnCode{

   @EqualsAndHashCode.Include
   private final String warnCode;
   private final String message;
   private final String messageDetail;
   private final String warnType;

   @Override
   public WarnCode getCurrentWarnCode() {
      return this;
   }

   public static class DefaultWarnBuilder{
      public DefaultWarnBuilder message(String template, Object...values){
         message = MessagesFormatter.format(template,values);
         return this;
      }
      public DefaultWarnBuilder messageDetail(String template, Object...values){
         messageDetail = MessagesFormatter.format(template,values);
         return this;
      }
   }
}
