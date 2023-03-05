package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Bid(
        BidInfo bid,
        @JsonProperty("escrow_account")
        Deployment.EscrowAccount escrowAccount
) {
}
