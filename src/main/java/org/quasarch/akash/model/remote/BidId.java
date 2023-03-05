package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BidId(
        String owner,
        @JsonProperty("dseq")
        String deploymentSequence,
        @JsonProperty("gseq")
        String groupSequence,
        @JsonProperty("oseq")
        String orderSequence,
        String provider
) {
}
