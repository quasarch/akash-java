package cloud.quasarch.akash.impl.model;

import cloud.quasarch.akash.impl.model.remote.AkashErrorType;

/**
 * Failure of a operation
 *
 * @param errorType           {@link AkashErrorType}
 * @param failureInnerMessage inner message
 */
public record OperationFailure(AkashErrorType errorType, String failureInnerMessage) {

}

