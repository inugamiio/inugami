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
package io.inugami.commons.files;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.loggers.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileUtils
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public class FilesUtils {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger              LOGGER              = LoggerFactory.getLogger(FilesUtils.class.getSimpleName());
    
    private static final Charset             UTF_8               = Charset.forName("UTF-8");
    
    private static final Charset             ISO_8859_1          = Charset.forName("ISO-8859-1");
    
    private static final Pattern             WINDOWS_PATH        = Pattern.compile("^[a-zA-z]+[:][\\\\]");
    
    private static final Map<String, String> FILES_MAPPING       = initFilesMapping();
    
    public static final int                  MEGA                = 1024 * 1024;
    
    public static final int                  DEFAULT_BUFFER_SIZE = 10 * MEGA;
    
    private static final String              UTF8                = "UTF-8";
    
    private final static Unzip               UNZIP               = new Unzip();
    
    // =========================================================================
    // ASSERTS
    // =========================================================================
    public static void assertFileExists(final File file) {
        Asserts.notNull("file mustn't be null!", file);
        Asserts.isTrue(String.format("file %s dosen't exists", getCanonicalPath(file)), file.exists());
    }
    
    private static Map<String, String> initFilesMapping() {
        final Map<String, String> result = new HashMap<>();
        result.put(".css", "text/css");
        result.put(".js", "application/javascript");
        result.put(".ts", "application/typescript");
        result.put(".svg", "image/svg+xml");
        result.put(".eot", "application/font-eot");
        result.put(".woff", "application/font-woff");
        result.put(".ttf", "application/font-ttf");
        
        return result;
    }
    
    public static void assertCanRead(final File file) {
        Asserts.notNull("file mustn't be null!", file);
        Asserts.isTrue(String.format("can't read file %s", getCanonicalPath(file)), file.canRead());
    }
    
    public static void assertCanWrite(final File file) {
        Asserts.notNull("file mustn't be null!", file);
        Asserts.isTrue(String.format("can't write file %s", getCanonicalPath(file)), file.canWrite());
    }
    
    public static void assertIsFolder(final File file) {
        Asserts.notNull("file mustn't be null!", file);
        Asserts.isTrue(String.format("file %s isn't folder", getCanonicalPath(file)), file.isDirectory());
    }
    
    public static void assertIsFile(final File file) {
        Asserts.notNull("file mustn't be null!", file);
        Asserts.isTrue(String.format("file %s isn't file", getCanonicalPath(file)), file.isFile());
    }
    
    // =========================================================================
    // BUILD FILES
    // =========================================================================
    public static File buildFile(final File file, final String... part) {
        return new File(buildPath(file, part));
    }
    
    public static String buildPath(final File file, final String... part) {
        final String[] parts = new String[part.length + 1];
        parts[0] = FilesUtils.getCanonicalPath(file);
        System.arraycopy(part, 0, parts, 1, part.length);
        return String.join(File.separator, parts);
    }
    
    // =========================================================================
    // FILE INFO
    // =========================================================================
    public static String getCanonicalPath(final File file) {
        Asserts.notNull("file mustn't be null!", file);
        try {
            return file.getCanonicalPath();
        }
        catch (final IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
    public static String getContentType(final File resource) {
        String result = null;
        if (resource != null) {
            
            try {
                final URLConnection stream = resource.toURI().toURL().openConnection();
                result = stream.getContentType();
            }
            catch (final IOException e) {
                Loggers.IO.error(e.getMessage());
            }
        }
        if ("content/unknown".equals(result)) {
            final String extension = resource.getPath().substring(resource.getPath().lastIndexOf("."));
            result = FILES_MAPPING.get(extension);
        }
        return result;
    }
    
    public static long getContentLength(final File resource) {
        long result = 0L;
        if (resource != null) {
            
            try {
                final URLConnection stream = resource.toURI().toURL().openConnection();
                result = stream.getContentLengthLong();
            }
            catch (final IOException e) {
                Loggers.IO.error(e.getMessage());
            }
        }
        return result;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static String readFileFromClassLoader(final String resourceName) {
        return readFileFromClassLoader(resourceName, UTF_8);
    }
    
    public static String readFileFromClassLoader(final String resourceName, final Charset charset) {
        String result = null;
        try {
            final byte[] data = new FilesUtils().readFromClassLoader(resourceName);
            result = new String(data, charset);
        }
        catch (final TechnicalException e) {
            throw new FatalException(e.getMessage(), e);
        }
        return result;
    }
    
    public static byte[] readFromClassLoader(final String resourceName) throws TechnicalException {
        Asserts.notNull(resourceName);
        final String realResourceName = resourceName.trim();
        byte[] result = null;
        final InputStream resource = FilesUtils.class.getClassLoader().getResourceAsStream(realResourceName);
        if (resource == null) {
            throw new FileUtilsException("can't found file {0} in classPath", realResourceName);
        }
        
        try {
            result = IOUtils.toByteArray(resource);
        }
        catch (final IOException e) {
            throw new FileUtilsException(e.getMessage(), e);
        }
        
        return result;
    }
    
    // =========================================================================
    // READ
    // =========================================================================
    public String read(final File file, final String encoding) throws IOException {
        return read(file, DEFAULT_BUFFER_SIZE, encoding);
    }
    
    public String read(final File file) throws IOException {
        return read(file, DEFAULT_BUFFER_SIZE, UTF8);
    }
    
    public String read(final File file, final int bufferSize) throws IOException {
        return read(file, bufferSize, UTF8);
    }
    
    public String read(final File file, final int bufferSize, final String encoding) throws IOException {
        Asserts.notNull(file);
        
        final StringBuilder data = processReading(file, bufferSize, encoding);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("done reading : nb chars : {}", data.length());
        }
        
        final String result = data.toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("reading done");
        }
        return result;
    }
    
    public static void readLineByLine(final File file, final Consumer<String> consumer) throws IOException {
        assertCanRead(file);
        Asserts.notNull("consumer is mandatory!", consumer);
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(file);
        }
        catch (final FileNotFoundException e) {
            throw e;
        }
        final BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        
        String line;
        
        do {
            try {
                line = br.readLine();
            }
            catch (final IOException e) {
                line = null;
            }
            if (line != null) {
                consumer.accept(line);
            }
        }
        while (line != null);
        
        try {
            br.close();
        }
        catch (final IOException e) {
            throw e;
        }
    }
    
    public static Map<String, String> readPropertiesInClassLoader(final String path) {
        final String content = readFileFromClassLoader(path, ISO_8859_1);
        return readProperties(content);
    }
    
    public static Map<String, String> readProperties(final String content) {
        Asserts.notNull("properties content mustn't be null!", content);
        final Reader reader = new StringReader(content);
        final Properties properties = new Properties();
        try {
            properties.load(reader);
        }
        catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
        
        final Map<String, String> result = new HashMap<>();
        properties.entrySet().forEach((entry) -> result.put(String.valueOf(entry.getKey()),
                                                            String.valueOf(entry.getValue())));
        return result;
    }
    
    private StringBuilder processReading(final File file, final int bufferSize,
                                         final String encoding) throws IOException {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(file, "r");
        }
        catch (final FileNotFoundException e) {
            throw new IOException(e.getMessage(), e);
        }
        finally {
            close(aFile);
        }
        StringBuilder data = null;
        final FileChannel inChannel = aFile.getChannel();
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            final long fileSize = inChannel.size();
            
            int fileRead = 0;
            
            final Double max = fileSize * 1.2;
            data = new StringBuilder(max.intValue());
            
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                final ReadPartResult readPartResult = readPart(buffer, fileRead, bufferSize, encoding);
                fileRead = readPartResult.getCursor();
                data.append(readPartResult.getData());
                buffer.clear();
                if ((fileRead % MEGA) == 0) {
                    LOGGER.info("read rest :  {}", fileSize - fileRead);
                }
            }
        }
        catch (final IOException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            throw e;
        }
        finally {
            inChannel.close();
            aFile.close();
        }
        
        return data;
    }
    
    private ReadPartResult readPart(final ByteBuffer buffer, final int oldCursor, final int bufferSize,
                                    final String encoding) throws UnsupportedEncodingException {
        int cursor = oldCursor;
        
        final byte[] data = new byte[bufferSize];
        for (int i = 0; i < buffer.limit(); i++) {
            data[i] = buffer.get();
            cursor++;
        }
        
        return new ReadPartResult(cursor, new String(data, encoding));
    }
    
    private class ReadPartResult {
        final int    cursor;
        
        final String data;
        
        public ReadPartResult(final int cursor, final String data) {
            super();
            this.cursor = cursor;
            this.data = data;
        }
        
        public int getCursor() {
            return cursor;
        }
        
        public String getData() {
            return data;
        }
        
    }
    
    // =========================================================================
    // WRITE
    // =========================================================================
    public static void write(final String content, final String file) throws FilesUtilsException {
        write(content, new File(file));
    }
    
    public static void write(final String content, final File file) throws FilesUtilsException {
        write(content, file, "UTF-8");
        
    }
    
    public static void write(final String content, final File file, final String encoding) throws FilesUtilsException {
        Asserts.notNull(file);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, encoding);
        }
        catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new FatalException(e.getMessage(), e);
        }
        
        Asserts.notNull(writer);
        try {
            if (content != null) {
                writer.println(content);
                Loggers.IO.info("write file : {}", file.getCanonicalFile().getAbsolutePath());
            }
        }
        catch (final IOException e) {
            Loggers.IO.error(e.getMessage());
        }
        finally {
            writer.close();
        }
        
    }
    
    public static URL convertToUrl(final File file) {
        Asserts.notNull(file);
        try {
            return file.toURI().toURL();
        }
        catch (final MalformedURLException e) {
            throw new FilesUtilsException(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // LIST FILES
    // =========================================================================
    public static List<File> list(final File folder) {
        return list(folder, null);
    }
    
    public static List<File> list(final File folder, final FilenameFilter filter) {
        assertCanRead(folder);
        assertIsFolder(folder);
        
        final String[] names = filter == null ? folder.list() : folder.list(filter);
        final List<String> filesNames = Arrays.asList(Optional.ofNullable(names).orElse(new String[0]));
        
        //@formatter:off
        return filesNames.stream()
                         .map(name -> buildFile(folder, name))
                         .collect(Collectors.toList());
        //@formatter:on
    }
    
    public static List<File> scanFilesystem(final File path, final FilenameFilter filter) {
        return scanFilesystem(path, filter, (file) -> true);
    }
    
    public static List<File> scanFilesystem(final File path, final FilenameFilter filter,
                                            final Predicate<File> directoryFilter) {
        final List<File> result = new ArrayList<>();
        if (path == null) {
            return result;
        }
        
        if (path.exists() && path.canRead()) {
            if (path.isDirectory()) {
                for (final String fileName : Optional.ofNullable(path.list()).orElse(new String[] {})) {
                    final File subPath = buildFile(path, fileName);
                    if (subPath.isFile() && filter.accept(path, fileName)) {
                        result.add(subPath);
                    }
                    else if (subPath.isDirectory() && directoryFilter.test(subPath)) {
                        result.addAll(scanFilesystem(subPath, filter));
                    }
                }
            }
            else if (filter.accept(path, path.getName())) {
                result.add(path);
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // SERIALIZABLE
    // =========================================================================
    public static <T extends Serializable> T readFromBinary(final File file, final T defaultValue) {
        T result = defaultValue;
        try {
            if (file.exists()) {
                final ObjectInputStream inputData = new ObjectInputStream(new FileInputStream(file));
                result = (T) inputData.readObject();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            Loggers.IO.error(e.getMessage());
        }
        return result;
    }
    
    public static void writeToBinary(final File file, final Serializable data) {
        try {
            final ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file));
            writer.writeObject(data);
        }
        catch (final IOException e) {
            Loggers.IO.error(e.getMessage());
        }
        
    }
    
    // =========================================================================
    // CHECKS
    // =========================================================================
    public static boolean isAbsoluteFile(final String path) {
        Asserts.notEmpty("Path mustn't be empty!", path);
        return path.startsWith(File.separator) || isWindowsPath(path);
    }
    
    private static boolean isWindowsPath(final String path) {
        return path.contains("\\") && WINDOWS_PATH.matcher(path).matches();
    }
    
    // =========================================================================
    // RESOLVE
    // =========================================================================
    public static File resolveJarFile(final URL url) {
        final String rawPath = url.getFile();
        final String pathWithoutProtocole = rawPath.replaceAll("file:", "");
        String jarPath = pathWithoutProtocole;
        if (pathWithoutProtocole.contains("!")) {
            jarPath = pathWithoutProtocole.substring(0, pathWithoutProtocole.lastIndexOf("!"));
        }
        return new File(jarPath);
    }
    
    // =========================================================================
    // DELEGATE
    // =========================================================================
    public static void unzip(final File zipFile, final File destination) throws IOException {
        UNZIP.unzip(zipFile, destination);
        LOGGER.info("unzip {} to {}", zipFile, destination);
    }
    
    public static void unzipLogless(final File zipFile, final File destination) throws IOException {
        UNZIP.unzipLogLess(zipFile, destination);
        LOGGER.info("unzip {} to {}", zipFile, destination);
    }
    
    public static boolean delete(final File file) {
        LOGGER.info("delete {}", file);
        boolean result = false;
        try {
            FileUtils.forceDelete(file);
            result = true;
        }
        catch (final IOException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }
    
    public static boolean copy(final File source, final File destination) {
        boolean result = false;
        LOGGER.info("copy {} to {}", source, destination);
        try {
            if (source.isDirectory()) {
                FileUtils.copyDirectory(source, destination);
            }
            else {
                FileUtils.copyFileToDirectory(source, destination);
            }
            result = true;
        }
        catch (final IOException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }
    
    public static String readContent(final File file) throws IOException {
        return new String(readBytes(file));
    }
    
    public static byte[] readBytes(final File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }
    
    public static File getTmpDir() {
        return new File(System.getProperty("java.io.tmpdir"));
    }
    
    public static boolean isWindows() {
        return "\\".equals(File.separator);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    public static void close(final Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            }
            catch (final IOException e) {
                Loggers.IO.error(e.getMessage());
            }
        }
    }
    
}
