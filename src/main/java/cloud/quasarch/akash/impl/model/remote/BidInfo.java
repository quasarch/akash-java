package cloud.quasarch.akash.impl.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * General info about a {@link Bid}
 * <a href="https://docs.akash.network/other-resources/marketplace#bid">Bid State</a>
 *
 * @param bidId                {@link BidId}
 * @param state                At this moment known values are: OPEN, ACTIVE, CLOSED TODO enum?!
 * @param price                Bid price - amount to be paid on every block.
 * @param createdAtBlockHeight time of creating of this bid
 */
public record BidInfo(
        @JsonProperty("bid_id")
        BidId bidId,
        String state,
        Price price,
        @JsonProperty("created_at")
        String createdAtBlockHeight

) {
}
