package org.quasarch.akash.uri;

import java.net.URI;
import java.net.URLEncoder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UriUtils {

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
