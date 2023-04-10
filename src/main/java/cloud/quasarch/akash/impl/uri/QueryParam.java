package cloud.quasarch.akash.impl.uri;

/**
 * A query param to be passed ( encoded ) to a {@link java.net.URI} instance
 * in the format name=value.
 *
 * @param name name of query parameter, which will be the key
 * @param value the value of the query parameter
 */
public record QueryParam(String name, String value) {

    String escaped() {
        //return URLEncoder.encode(name + "=" + value, StandardCharsets.UTF_8);
        return name + "=" + value;
    }

    boolean hasValue() {
        return value != null;
    }
}
