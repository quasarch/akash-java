package cloud.quasarch.akash.impl.model.remote;

/**
 * Types of error that can occur while interacting with akash network.
 */
public enum AkashErrorType {
    /**
     * Error related to wire level data transfer. It can/should be retried
     */
    AkashNetworkError,
    /**
     * Authentication/Authorization related error; a retry is not advised without renegotiation of
     * authentication tokens.
     */
    AuthError,
    /**
     * The current state of transaction/request doesn't allow the requested operation.
     */
    StateError,
    /**
     * The request is malformed, invalid. Should not be retried.
     */
    BadRequest,
    /**
     * Error related to the client
     * TODO
     */
    ClientError
}
