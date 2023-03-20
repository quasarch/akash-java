package org.quasarch.akash.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Identifies a {@link Bid}
 * <a href="https://docs.akash.network/other-resources/marketplace#bid">BidId</a>
 *
 * @param owner              account addres of tenant
 * @param deploymentSequence Arbitrary sequence number that identifies the deployment. Defaults to block height.
 * @param groupSequence      Arbitrary sequence number. Internally incremented, starting at 1.
 * @param orderSequence      Arbitrary sequence number. Internally incremented, starting at 1.
 * @param provider           Account address of provider.
 */
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
