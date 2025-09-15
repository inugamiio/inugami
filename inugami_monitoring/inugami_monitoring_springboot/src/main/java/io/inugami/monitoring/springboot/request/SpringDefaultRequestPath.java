package io.inugami.monitoring.springboot.request;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;

public class SpringDefaultRequestPath implements RequestPath {

    private final PathContainer fullPath;

    private final PathContainer contextPath;

    private final PathContainer pathWithinApplication;


    public SpringDefaultRequestPath(String rawPath, @Nullable String contextPath) {
        this.fullPath = PathContainer.parsePath(rawPath);
        this.contextPath = initContextPath(this.fullPath, contextPath);
        this.pathWithinApplication = extractPathWithinApplication(this.fullPath, this.contextPath);
    }

    private SpringDefaultRequestPath(RequestPath requestPath, String contextPath) {
        this.fullPath = requestPath;
        this.contextPath = initContextPath(this.fullPath, contextPath);
        this.pathWithinApplication = this.contextPath == null ? null : extractPathWithinApplication(this.fullPath,
                                                                                                    this.contextPath);
    }

    private static PathContainer initContextPath(PathContainer path, @Nullable String contextPath) {
        if (path == null) {
            return null;
        }

        if (!StringUtils.hasText(contextPath) || "/".equals(path.value())) {
            return PathContainer.parsePath("");
        }

        validateContextPath(path.value(), contextPath);

        int length  = contextPath.length();
        int counter = 0;

        for (int i = 0; i < path.elements().size(); i++) {
            PathContainer.Element element = path.elements().get(i);
            counter += element.value().length();
            if (length == counter) {
                return path.subPath(0, i + 1);
            }
        }

        // Should not happen..
        throw new IllegalStateException(
                "Failed to initialize contextPath '" + contextPath + "'" + " for requestPath '" + path.value() + "'");
    }

    private static void validateContextPath(String fullPath, String contextPath) {
        int length = contextPath.length();
        if (contextPath.charAt(0) != '/' || contextPath.charAt(length - 1) == '/') {
            throw new IllegalArgumentException(
                    "Invalid contextPath: '" + contextPath + "': " + "must start with '/' and not end with '/'");
        }
        if (!fullPath.startsWith(contextPath)) {
            throw new IllegalArgumentException(
                    "Invalid contextPath '" + contextPath + "': " + "must match the start of requestPath: '" +
                    fullPath + "'");
        }
        if (fullPath.length() > length && fullPath.charAt(length) != '/') {
            throw new IllegalArgumentException("Invalid contextPath '" + contextPath + "': " +
                                               "must match to full path segments for requestPath: '" + fullPath + "'");
        }
    }

    private static PathContainer extractPathWithinApplication(PathContainer fullPath, PathContainer contextPath) {
        return fullPath.subPath(contextPath.elements().size());
    }


    // PathContainer methods..

    @Override
    public String value() {
        return this.fullPath.value();
    }

    @Override
    public List<Element> elements() {
        return this.fullPath.elements();
    }


    // RequestPath methods..

    @Override
    public PathContainer contextPath() {
        return this.contextPath;
    }

    @Override
    public PathContainer pathWithinApplication() {
        return this.pathWithinApplication;
    }

    @Override
    public RequestPath modifyContextPath(String contextPath) {
        return new SpringDefaultRequestPath(this, contextPath);
    }


    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        SpringDefaultRequestPath otherPath = (SpringDefaultRequestPath) other;
        return (this.fullPath.equals(otherPath.fullPath) && this.contextPath.equals(otherPath.contextPath) &&
                this.pathWithinApplication.equals(otherPath.pathWithinApplication));
    }

    @Override
    public int hashCode() {
        int result = this.fullPath.hashCode();
        result = 31 * result + this.contextPath.hashCode();
        result = 31 * result + this.pathWithinApplication.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.fullPath.toString();
    }

}
