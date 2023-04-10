package cloud.quasarch.akash.impl.parsing;

import io.vavr.control.Either;
import cloud.quasarch.akash.impl.model.OperationFailure;

import java.net.http.HttpResponse;

/**
 * ResponseParser goal is to deal with the transformation of a plain json response into Either a {@link OperationFailure}
 * or a concrete response.
 *
 * @param <R> The expected return type on "right"/success cases.
 */
public interface ResponseParser<R> {
    /**
     * @param response {@link HttpResponse} with string
     * @return Either R or the failure
     */
    Either<OperationFailure, R> parseToEither(HttpResponse<String> response);

    /**
     * @param statusCode integer with the http status code
     * @param body       body in string representation
     * @return Either R or the failure
     */
    Either<OperationFailure, R> parseErrorMessage(int statusCode, String body);

}
