package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BidInfo(
        @JsonProperty("bid_id")
        BidId bidId,
        String state,
        Price price,
        @JsonProperty("created_at")
        String createdAtBlockHeight

) {
}
