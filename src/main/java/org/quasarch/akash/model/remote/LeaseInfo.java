package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public record LeaseInfo(
        @JsonProperty("lease_id")
        LeaseId leaseId,
        String state,
        Price price,
        @JsonProperty("created_at")
        String createdAtBlockHeight,
        @JsonProperty(value = "closed_on", required = false)
        @Nullable
        String closedAtBlockHeight

) {

    public record LeaseId(String owner,
                          @JsonProperty("dseq")
                          String deploymentSequence,
                          @JsonProperty("gseq")
                          String groupSequence,
                          @JsonProperty("oseq")
                          String orderSequence,
                          @JsonProperty("provider")
                          String provider) {
    }

}
