package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @param nextKey next page identifier
 * @param total   count
 */
public record PageInfo(
        @JsonProperty("next_key") String nextKey,
        int total
) {
}
