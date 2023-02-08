package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PageInfo(
        @JsonProperty("next_key") String nextKey,
        int total
) {
}
