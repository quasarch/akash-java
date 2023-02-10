package org.quasarch.akash.impl.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.remote.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;
import java.util.function.Function;

/**
 * TODO Document!
 * TODO NO_OP transformation
 *
 * @param <I> the intermediary representation type, which is the representation of the json
 * @param <R> the final result object, after applying transformation.
 */
public class ResponseParserWithTransformation<I, R> implements ResponseParser<R> {
    private static final Logger log = LoggerFactory.getLogger(ResponseParserWithTransformation.class);

    private final Class<I> jsonModelClass;
    private final Function<I, R> transformation;

    public ResponseParserWithTransformation(Class<I> jsonModelClass, Function<I, R> transformation) {
        this.jsonModelClass = jsonModelClass;
        this.transformation = transformation;
    }

    public Either<OperationFailure, R> parseToEither(HttpResponse<String> response) {
        final var body = response.body();
        if (response.statusCode() / 100 != 2) {
            return parseErrorMessage(response.statusCode(), body);
        }

        try {
            log.trace("received response string: {}", body);
            var deploymentResponse = ObjectMapperFactory.getInstance().readValue(body, jsonModelClass);
            return Either.right(transformation.apply(deploymentResponse));
        } catch (
                JsonProcessingException jsonEx) {
            return Either.left(OperationFailure.from(jsonEx));
        }
    }

    public Either<OperationFailure, R> parseErrorMessage(int statusCode, String body) {
        try {
            log.trace("received response error: {}", body);
            var error = ObjectMapperFactory.getInstance().readValue(body, ErrorResponse.class);
            return Either.left(OperationFailure.from(error));
        } catch (JsonProcessingException jsonEx) {
            return Either.left(OperationFailure.from(jsonEx));
        }
    }

    /**
     * contains singleton {@link ObjectMapper}
     */
    private static class ObjectMapperFactory {
        private static ObjectMapper obj = new ObjectMapper();

        public static ObjectMapper getInstance() {
            return obj;
        }
    }
}
