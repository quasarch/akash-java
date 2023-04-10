package cloud.quasarch.akash.impl.model.remote;

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
