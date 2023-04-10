package cloud.quasarch.akash.impl.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @param leaseInfo     {@link LeaseInfo}
 * @param escrowPayment {@link cloud.quasarch.akash.impl.model.remote.Deployment.EscrowAccount}
 */
public record DeploymentLease(
        @JsonProperty("lease")
        LeaseInfo leaseInfo,
        @JsonProperty("escrow_payment")
        EscrowPayment escrowPayment


) {
    /**
     * @param owner     who requested it
     * @param state     Account state.
     * @param accountId PaymentID
     * @param paymentId Unique (over AccountID) ID of payment.
     * @param rate      {@link Price} Tokens per block to transfer.
     * @param balance   {@link Price} Balance currently reserved for owner.
     * @param withdrawn {@link Price} Amount already withdrawn by owner.
     */
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

