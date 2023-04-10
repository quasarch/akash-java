package cloud.quasarch.akash.impl.uri;

import java.net.URI;
import java.util.stream.Stream;

/**
 * Utility class for operations related to Uri class
 */
public class UriUtils {
    /**
     * no op
     */
    private UriUtils() {
    }

    /**
     * Adds one or multiple query parameter to a uri.
     *
     * @param basePath path to which query params will be appended
     * @param params   to append
     * @return a new URL with basePath?params
     */
    public static URI addQueryParameters(URI basePath, QueryParam... params) {

        final boolean hasQueryAlready = basePath.getQuery() != null && !basePath.getQuery().isEmpty();
        var querySection = Stream
                .of(params)
                .filter(QueryParam::hasValue)
                .collect(() -> new StringBuilder(hasQueryAlready ? basePath.getQuery() + "&" : ""),
                        (result, param) -> result.append(param.escaped()).append("&"), StringBuilder::append)
                .toString();
        querySection = querySection.substring(0, querySection.length() - 1);


        return basePath.resolve(
                basePath.getPath() + "?" + querySection
        );
    }
}
