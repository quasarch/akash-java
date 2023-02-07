package org.quasarch.akash.uri;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A query param to be passed ( encoded ) to a {@link java.net.URI} instance
 * in the format name=value.
 *
 * @param name
 * @param value
 */
public record QueryParam(String name, String value) {

    String escaped() {
        return URLEncoder.encode(name + "=" + value, StandardCharsets.UTF_8);
    }
}
