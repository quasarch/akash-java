package org.quasarch.akash.impl.parsing;

import io.vavr.control.Either;
import org.quasarch.akash.model.OperationFailure;

import java.net.http.HttpResponse;

/**
 * ResponseParser goal is to deal with the transformation of a plain json response into Either a {@link OperationFailure}
 * or a concrete response.
 *
 * @param <R> The expected return type on "right"/success cases.
 */
public interface ResponseParser<R> {
    Either<OperationFailure, R> parseToEither(HttpResponse<String> response);

    Either<OperationFailure, R> parseErrorMessage(int statusCode, String body);

}
