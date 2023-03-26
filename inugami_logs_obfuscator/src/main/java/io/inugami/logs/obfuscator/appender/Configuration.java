package io.inugami.logs.obfuscator.appender;

public class Configuration {
    private boolean encodeAsJson;
    private String  additionalFields;
    private String  file;

    public Configuration() {
    }

    public Configuration(final String encodeAsJson, final String additionalFields) {
        this.encodeAsJson = Boolean.parseBoolean(encodeAsJson);
        this.additionalFields = additionalFields;
    }

    public Configuration(final boolean encodeAsJson, final String additionalFields) {
        this.encodeAsJson = encodeAsJson;
        this.additionalFields = additionalFields;
    }

    @Override
    public String toString() {
        return "ObfuscatorEncoderConfiguration{" +
                "encodeAsJson=" + encodeAsJson +
                ", additionalFields='" + additionalFields + '\'' +
                '}';
    }

    public boolean isEncodeAsJson() {
        return encodeAsJson;
    }

    public void setEncodeAsJson(final boolean encodeAsJson) {
        this.encodeAsJson = encodeAsJson;
    }

    public String getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(final String additionalFields) {
        this.additionalFields = additionalFields;
    }

    public String getFile() {
        return file;
    }

    public void setFile(final String file) {
        this.file = file;
    }
}
