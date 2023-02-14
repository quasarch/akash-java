package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeploymentLease(
        @JsonProperty("lease")
        LeaseInfo leaseInfo,
        @JsonProperty("escrow_payment")
        EscrowPayment escrowPayment


) {
    public record EscrowPayment(String owner,
                                String state,
                                @JsonProperty("account_id")
                                AccountId accountId,
                                @JsonProperty("payment_id")
                                String paymentId,
                                Price rate,
                                Price balance,
                                Price withdrawn
    ) {


    }
}

