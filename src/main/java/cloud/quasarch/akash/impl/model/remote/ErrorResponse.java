package cloud.quasarch.akash.impl.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Information of an error in a request
 *
 * @param error   short error name
 * @param code    unique code
 * @param message more error information
 * @param details error details
 */
public record ErrorResponse(String error, Integer code,
                            String message,

                            Collection<ErrorResponseDetails> details) {
    /**
     *
     * @param typeUrl url for type
     * @param value response detail
     */
    public record ErrorResponseDetails(@JsonProperty("type_url") String typeUrl,
                                       String value) {
    }
}

