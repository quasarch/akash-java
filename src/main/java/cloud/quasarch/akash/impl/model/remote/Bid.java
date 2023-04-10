package cloud.quasarch.akash.impl.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Akash uses a reverse auction. Tenants set the price and terms of their deployment,
 * and the Cloud providers bid on the deployments.
 * For more information see <a href="https://docs.akash.network/other-resources/marketplace">...</a>
 *
 * @param bid           offered by the provider. The 'price' it charges for the asked deployment
 * @param escrowAccount a mechanism that allow for time-based payments
 */
public record Bid(
        BidInfo bid,
        @JsonProperty("escrow_account")
        Deployment.EscrowAccount escrowAccount
) {
}
