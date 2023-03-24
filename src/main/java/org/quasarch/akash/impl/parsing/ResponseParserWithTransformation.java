package org.quasarch.akash.impl.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.remote.AkashErrorType;
import org.quasarch.akash.model.remote.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Function;

import static org.quasarch.akash.model.remote.AkashErrorType.*;

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

    /**
     * constructor
     *
     * @param jsonModelClass response type, class
     * @param transformation transformation function, from http response type
     */
    public ResponseParserWithTransformation(Class<I> jsonModelClass, Function<I, R> transformation) {
        this.jsonModelClass = jsonModelClass;
        this.transformation = transformation;
    }

    /**
     * transforms a {@link HttpResponse} into a varv Either.
     *
     * @param response {@link HttpResponse} with string
     * @return Either
     */
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
            return Either.left(new OperationFailure(ClientError, jsonEx.getMessage()));
        }
    }

    public Either<OperationFailure, R> parseErrorMessage(int statusCode, String body) {
        try {
            log.trace("received response error: {}", body);
            var error = ObjectMapperFactory.getInstance().readValue(body, ErrorResponse.class);
            AkashErrorType errorType = AkashNetworkError;
            if (BAD_REQUEST_CODES.contains(error.code())) {
                errorType = BadRequest;
            } else if (AUTH_ERROR_CODES.contains(error.code())) {
                errorType = AuthError;
            } else if (STATE_ERROR_CODES.contains(error.code())) {
                errorType = StateError;
            }


            return Either.left(new OperationFailure(errorType, error.message()));
        } catch (JsonProcessingException jsonEx) {
            return Either.left(new OperationFailure(ClientError, jsonEx.getMessage()));
        }
    }

    //   private static final List<Integer> AKASH_NETWORK_ERROR_CODES = List.of(1, 2, 20, 35, 39);
    private static final List<Integer> AUTH_ERROR_CODES = List.of(4, 23);
    private static final List<Integer> BAD_REQUEST_CODES = List.of(3, 6, 7, 8, 9, 10, 12, 15, 16, 17, 18, 19
            , 21, 22, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 36, 37, 38);
    private static final List<Integer> STATE_ERROR_CODES = List.of(5, 11, 13, 25);


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
