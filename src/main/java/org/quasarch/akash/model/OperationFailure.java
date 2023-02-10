package org.quasarch.akash.model;

import org.quasarch.akash.model.remote.ErrorResponse;

public record OperationFailure(String failureMessage) {
    public static OperationFailure from(Exception io) {
        return new OperationFailure(io.toString());
    }

    public static OperationFailure from(ErrorResponse response) {
        return new OperationFailure(response.code() + " " + response.message());
    }
}

