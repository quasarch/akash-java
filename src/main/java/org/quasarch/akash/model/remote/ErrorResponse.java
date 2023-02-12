package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record ErrorResponse(String error, Integer code,
                            String message,

                            Collection<ErrorResponseDetails> details) {

    public record ErrorResponseDetails(@JsonProperty("type_url") String typeUrl,
                                       String value) {
    }
}

