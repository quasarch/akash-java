package org.quasarch.akash.uri;

import java.net.URI;
import java.net.URLEncoder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UriUtils {

    public static URI addQueryParameters(URI basePath, QueryParam... params) {
        var querySection = Stream
                .of(params)
                .filter(QueryParam::hasValue)
                .reduce(new StringBuilder("?"),
                        (result, param) -> result.append(param.escaped()).append("&"), StringBuilder::append)
                .toString();

        return basePath.resolve(
                basePath.getPath() + querySection
        );
    }
}
