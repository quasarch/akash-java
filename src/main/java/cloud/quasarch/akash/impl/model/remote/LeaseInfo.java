package cloud.quasarch.akash.impl.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * @param leaseId              The same as the bid ID for the lease.
 * @param state                ACTIVE, CLOSED
 * @param price                {@link Price}
 * @param createdAtBlockHeight identifies lease creation moment
 * @param closedAtBlockHeight  identifies lease close moment
 */
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

    /**
     * @param owner              who requested it
     * @param deploymentSequence Arbitrary sequence number that identifies the deployment. Defaults to block height.
     * @param groupSequence      Arbitrary sequence number. Internally incremented, starting at 1.
     * @param orderSequence      Arbitrary sequence number. Internally incremented, starting at 1.
     * @param provider           Account address of provider.
     */
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
