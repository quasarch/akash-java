package org.quasarch.akash.model;

public record OperationFailure(String failureMessage) {
    public static OperationFailure from(Exception io) {
        return new OperationFailure(io.toString());
    }
}
