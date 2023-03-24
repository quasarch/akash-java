package org.quasarch.akash.model;

import org.quasarch.akash.model.remote.AkashErrorType;

/**
 * Failure of a operation
 *
 * @param errorType           {@link AkashErrorType}
 * @param failureInnerMessage inner message
 */
public record OperationFailure(AkashErrorType errorType, String failureInnerMessage) {

}

