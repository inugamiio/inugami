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
package io.inugami.configuration.services.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.inugami.api.processors.ConfigHandler;

/**
 * FunctionsServices
 * 
 * @author patrick_guillerm
 * @since 17 août 2017
 */
public class FunctionsServices {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String KEY_FUNCTION_NAME   = "name";
    
    private final static String KEY_FUNCTION_PARAMS = "params";
    
    //@formatter:off
    private final static Pattern REGEX_FIND_FUNCTION = Pattern.compile("(?:[#][{])(?<name>([^(]+))(?:[(])(?<params>([^)]*))(?:[)])(?:[}])");
    //@formatter:on
    
    private final List<ProviderAttributFunction> functions;
    
    private final ConfigHandler<String, String>  config;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    protected FunctionsServices() {
        this(new ProviderAttributFunction[] {}, null);
    }
    
    public FunctionsServices(final ConfigHandler<String, String> config) {
        super();
        this.functions = new ArrayList<>();
        this.config = config;
    }
    
    public FunctionsServices(final List<ProviderAttributFunction> functions,
                             final ConfigHandler<String, String> config) {
        super();
        this.functions = functions;
        this.config = config;
    }
    
    public FunctionsServices(final ProviderAttributFunction[] functions, final ConfigHandler<String, String> config) {
        super();
        this.functions = functions == null ? new ArrayList<>() : Arrays.asList(functions);
        this.config = config;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public String applyFunctions(final String value) {
        String result = value;
        
        if (containsFunction(value)) {
            final List<FunctionData> data = extractData(value);
            
            final List<FunctionResultPart> functionResults = new ArrayList<>();
            for (final FunctionData subData : data) {
                final ProviderAttributFunction function = loadFunction(subData);
                if (function != null) {
                    final String functionResult = function.apply(subData);
                    functionResults.add(new FunctionResultPart(subData.getStart(), subData.getEnd(), functionResult));
                    
                }
            }
            
            final StringBuilder content = new StringBuilder();
            final int size = functionResults.size();
            for (int i = 0; i < size; i++) {
                final FunctionResultPart functionContent = functionResults.get(i);
                if (i == 0) {
                    content.append(value.substring(0, functionContent.getStart()));
                }
                content.append(functionContent.getContent());
                if (i < (size - 1)) {
                    content.append(value.substring(functionContent.getEnd(), functionResults.get(i + 1).getStart()));
                }
                if (i == (size - 1)) {
                    content.append(value.substring(functionContent.getEnd()));
                }
            }
            result = content.toString();
            
        }
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    /* package */ boolean containsFunction(final String value) {
        return REGEX_FIND_FUNCTION.matcher(value).find();
    }
    
    /* package */ List<FunctionData> extractData(final String value) {
        final List<FunctionData> data = new ArrayList<>();
        final Matcher matcher = REGEX_FIND_FUNCTION.matcher(value);
        
        while (matcher.find()) {
            final String functionName = matcher.group(KEY_FUNCTION_NAME);
            final String rawParams = matcher.group(KEY_FUNCTION_PARAMS);
            final String[] params = cleanParams(rawParams);
            data.add(new FunctionData(functionName, params, matcher.start(), matcher.end()));
        }
        
        return data;
    }
    
    /* package */ ProviderAttributFunction loadFunction(final FunctionData data) {
        ProviderAttributFunction result = null;
        if (functions != null) {
            for (final ProviderAttributFunction function : functions) {
                if (function.getName().equals(data.getFunctionName())) {
                    result = function;
                    break;
                }
            }
        }
        return result;
    }
    
    private String[] cleanParams(final String rawParams) {
        String[] result = null;
        if (rawParams != null) {
            final List<String> clean = new ArrayList<>();
            final String[] items = rawParams.split(",");
            
            for (final String item : items) {
                if (item != null) {
                    clean.add(processCleaning(item));
                }
            }
            
            final String[] type = {};
            result = clean.toArray(type);
        }
        
        return result;
    }
    
    private String processCleaning(final String item) {
        final String value = config.applyProperties(item);
        return value.trim();
    }
}
