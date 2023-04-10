package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.*;
import java.nio.charset.Charset;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class FeignBody implements Response.Body {
    private final byte[] body;

    @Override
    public Integer length() {
        return body.length;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public InputStream asInputStream() throws IOException {
        return new ByteArrayInputStream(body);
    }

    @Override
    public Reader asReader(final Charset charset) throws IOException {
        return new InputStreamReader(asInputStream());
    }

    @Override
    public void close() throws IOException {

    }
}
