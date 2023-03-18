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
package io.inugami.commons.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.engine.js.JavaScriptEngineFunctions;
import io.inugami.commons.engine.js.objects.JsNamespaceFunction;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.marshaling.JsonMarshaller;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JavaScriptEngine
 * 
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
public class JavaScriptEngine implements ErrorReporter {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Map<Integer, ScriptCheckingDto> CHECK_CACHE      = new ConcurrentHashMap<>();
    
    private final File                                   allScriptFile;
    
    private final ScriptEngineManager                    factory          = new ScriptEngineManager();
    
    private final ScriptEngine                           engine           = factory.getEngineByName("nashorn");
    
    private final Map<String, String>                    scriptReferencer = new LinkedHashMap<>();
    
    private String                                       referencedScript;
    
    private static final Map<String, String>             FUNCTIONS        = new ConcurrentHashMap<>();
    
    private static final JavaScriptEngine                INSTANCE         = new JavaScriptEngine();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    /* package */ JavaScriptEngine() {
        super();
        final String allScriptPath = JvmKeyValues.SCRIPT_FULL_SCRIPT_PATH.get();
        allScriptFile = allScriptPath == null ? null : new File(allScriptPath);
        
        // @formatter:off
        registerFromClassLoader("META-INF/javaScriptEngineFunctions.js",
                                "META-INF/javaScriptEngineDataExtractor.js");
        
        final List<JavaScriptEngineScriptLoaderSpi> loaders = SpiLoader.getInstance().loadSpiService(JavaScriptEngineScriptLoaderSpi.class);
        Optional.ofNullable(loaders)
                .orElse(new ArrayList<>())
                .stream()
                .map(JavaScriptEngineScriptLoaderSpi::scriptsToLoad)
                .flatMap(List::stream)
                .forEach(this::registerFromClassLoader);
        
        // @formatter:on
        
        try {
            checkScript(referencedScript);
        }
        catch (final ScriptException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    public static JavaScriptEngine getInstance() {
        return INSTANCE;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public final synchronized void checkScript(final String script) throws ScriptException {
        final int hash = script.hashCode();
        final ScriptCheckingDto error = CHECK_CACHE.get(hash);
        ScriptException exception = null;
        if (error == null) {
            try {
                final String fullScript = new StringBuilder(referencedScript).append(script).toString();
                engine.eval(fullScript);
                CHECK_CACHE.put(hash, new ScriptCheckingDto());
            }
            catch (final ScriptException e) {
                exception = e;
                CHECK_CACHE.put(hash, new ScriptCheckingDto(e));
            }
        }
        else {
            exception = error.getError();
        }
        if (exception != null) {
            throw exception;
        }
    }
    
    public void checkScriptInnerFunction(final String script) throws ScriptException {
        checkScript(buildFunction("check", script));
    }
    
    // =========================================================================
    // EXECUTE
    // =========================================================================
    public String execute(final String functionName) throws ScriptException {
        return execute(null, functionName, new Object[] {});
    }
    
    public String execute(final String functionName, final Object... params) throws ScriptException {
        return execute(null, functionName, params);
    }
    
    public String execute(final String script, final String functionName) throws ScriptException {
        return execute(script, functionName, new Object[] {});
    }
    
    public String execute(final String script, final String functionName,
                          final Object... params) throws ScriptException {
        return (String) processExecute(script, functionName, true, params);
    }
    
    public Object processExecute(final String script, final String functionName, final boolean transcode,
                                 final Object... params) throws ScriptException {
        Object result = null;
        if (functionName == null) {
            return result;
        }
        final Chrono chrono = Chrono.startChrono();
        
        try {
            engine.eval(referencedScript);
            if (script != null) {
                engine.eval(script);
            }
            Object jsResult = null;
            final JsNamespaceFunction namespace = new JsNamespaceFunction(functionName);
            
            if (namespace.getNamespace().isPresent()) {
                final Object function = engine.eval(namespace.getNamespace().get());
                final Invocable invocable = (Invocable) engine;
                if (function != null) {
                    jsResult = invocable.invokeMethod(function, namespace.getFunction(), params);
                }
                else {
                    Loggers.SCRIPTS.error("function {} doesn't exists", functionName);
                }
            }
            else {
                final Invocable invocable = (Invocable) engine;
                jsResult = invocable.invokeFunction(functionName, params);
            }
            
            result = !transcode ? jsResult : JavaScriptEngineFunctions.stringify(jsResult);
            
        }
        catch (final NoSuchMethodException e) {
            Loggers.SCRIPTS.error(e.getMessage(), e);
            throw new ScriptException(e.getMessage());
        }
        catch (final ScriptException e) {
            Loggers.SCRIPTS.error(e.getMessage(), e);
            throw e;
        }
        finally {
            chrono.stop();
            Loggers.SCRIPTS.info("running script on :{}ms", chrono.getDelaisInMillis());
        }
        
        return result;
    }
    
    // =========================================================================
    // ADD/GET FUNCTION
    // =========================================================================
    public String getFunction(final String functionName) {
        return FUNCTIONS.get(functionName);
    }
    
    public void addFunction(final String functionName, final String script) {
        FUNCTIONS.put(functionName, script);
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    public String buildFunction(final String name, final String content, final String... params) {
        final JsonBuilder js = new JsonBuilder();
        js.write("function ");
        js.write(name);
        js.openTuple();
        for (int i = 0; i < params.length; i++) {
            if (i != 0) {
                js.addSeparator();
            }
            js.write(params[i]);
        }
        js.closeTuple();
        js.openObject();
        js.write(content);
        js.closeObject();
        return js.toString();
    }
    
    public void registerFromClassLoader(final String... paths) {
        for (final String path : paths) {
            register(FilesUtils.readFileFromClassLoader(path), path);
        }
        
    }
    
    public synchronized void register(final String script, final String name) {
        scriptReferencer.put(name, script);
        buildReferencedScript();
    }
    
    private void buildReferencedScript() {
        final JsonBuilder script = new JsonBuilder();
        for (final Map.Entry<String, String> entry : scriptReferencer.entrySet()) {
            script.addLine();
            script.openComment();
            script.write(entry.getKey());
            script.closeComment();
            script.addLine();
            script.write(entry.getValue()).addLine();
        }
        
        referencedScript = script.toString();
        
        if (allScriptFile != null) {
            FilesUtils.write(referencedScript, allScriptFile);
        }
    }
    
    private boolean isDevMode() {
        return "dev".equals(JvmKeyValues.ENVIRONMENT.get());
    }
    
    @Override
    public void error(final String arg0, final String arg1, final int arg2, final String arg3, final int arg4) {
        Loggers.SCRIPTS.error("{} {} {} {}", arg0, arg1, arg2, arg3, arg4);
        
    }
    
    @Override
    public EvaluatorException runtimeError(final String arg0, final String arg1, final int arg2, final String arg3,
                                           final int arg4) {
        Loggers.SCRIPTS.error("{} {} {} {}", arg0, arg1, arg2, arg3, arg4);
        return null;
    }
    
    @Override
    public void warning(final String arg0, final String arg1, final int arg2, final String arg3, final int arg4) {
        Loggers.SCRIPTS.warn("{} {} {} {}", arg0, arg1, arg2, arg3, arg4);
    }
    
    public boolean validateJson(final String json) {
        boolean result = false;
        try {
            if (json != null) {
                buildJsonObject(json);
            }
            result = true;
        }
        catch (final IOException e) {
            Loggers.DEBUG.trace(e.getMessage(), e);
        }
        return result;
    }
    
    private void buildJsonObject(final String json) throws IOException {
        JsonMarshaller.getInstance().getDefaultObjectMapper().readTree(json);
    }
    
}
