package org.quasarch.akash.model;

import org.quasarch.akash.model.remote.AkashErrorType;

public record OperationFailure(AkashErrorType errorType, String failureInnerMessage) {

}

