package cloud.quasarch.akash.impl.model.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Deployment information, as specified in:
 * <a href="https://docs.akash.network/other-resources/marketplace#deployment">Deployment</a>
 *
 * @param deployment    {@link DeploymentInfo}
 * @param groups        {@link Group}
 * @param escrowAccount {@link EscrowAccount}
 */
public record Deployment(
        DeploymentInfo deployment,
        Collection<Group> groups,
        @JsonProperty("escrow_account")
        EscrowAccount escrowAccount
) {
    /**
     * @param deploymentId         ID of Deployment.
     * @param state                Version
     * @param version              Hash of the manifest that is sent to the providers.
     * @param createdAtBlockHeight creation time for this deployment
     */
    public record DeploymentInfo(
            @JsonProperty("deployment_id")
            DeploymentId deploymentId,
            String state,
            String version,

            // is this a sequence?
            @JsonProperty("created_at")
            String createdAtBlockHeight) {
    }

    /**
     * Generic tuple
     *
     * @param key   k
     * @param value v
     */
    public record Attribute(String key,
                            String value) {
    }

    /**
     * Amount deposited from owner account.
     *
     * @param denom  denomination  todo dunno
     * @param amount todo amount
     */
    public record Balance(String denom,
                          String amount
    ) {
    }

    /**
     * Describes cpu resources in the context of the deployment
     *
     * @param units      {@link Units}
     * @param attributes {@link Attribute}
     */
    public record Cpu(Units units,
                      Collection<Attribute> attributes) {

    }

    /**
     * Identifies a deployment, mainly who requested it and the sequence
     *
     * @param owner              who requested it
     * @param deploymentSequence sequence
     */
    public record DeploymentId(
            String owner,
            @JsonProperty("dseq")
            String deploymentSequence
    ) {
    }

    /**
     * @param kind           like ip
     * @param sequenceNumber Arbitrary sequence number.
     */
    public record Endpoint(
            String kind,
            @JsonProperty("sequence_number")
            int sequenceNumber
    ) {
    }

    /**
     * Escrow accounts are a mechanism that allow for time-based payments from one account
     * to another without block-by-block micropayments.
     * They also support holding funds for an account until an arbitrary event occurs.
     * More on <a href="https://docs.akash.network/other-resources/marketplace#group">...</a>
     *
     * @param id          Unique ID of account.
     * @param owner       Account address of owner.
     * @param state       Account state.
     * @param balance     {@link Balance}
     * @param transferred {@link Transferred}
     * @param settledAt   Last block that payments were settled.
     * @param depositor   Tenants are required to submit a deposit when creating a deployment
     * @param funds       {@link Funds}
     */
    public record EscrowAccount(
            Id id,
            String owner,
            String state,
            Balance balance,
            Transferred transferred,
            @JsonProperty("settled_at")
            String settledAt,
            String depositor,
            Funds funds

    ) {
    }

    /**
     * @param denom  denomination  denomination
     * @param amount how much
     */
    public record Funds(String denom, String amount) {
    }

    /**
     * @param groupId              {@link GroupId}
     * @param state                State of the group.
     * @param groupSpec            {@link GroupSpec}
     * @param createdAtBlockHeight creation time of this group
     */
    public record Group(
            @JsonProperty("group_id")
            GroupId groupId,
            String state,
            @JsonProperty("group_spec")
            GroupSpec groupSpec,
            // todo what is created_at? Is this block height? What does this format represent?
            // is this a sequence?
            @JsonProperty("created_at")
            String createdAtBlockHeight
    ) {
    }

    /**
     * @param owner              Provider ID to withdraw funds for.
     * @param deploymentSequence Deployment ID of group.
     * @param groupSequence      Arbitrary sequence number. Internally incremented, starting at 1.
     */
    public record GroupId(
            String owner,
            @JsonProperty("dseq")
            String deploymentSequence,
            @JsonProperty("gseq")
            int groupSequence
    ) {
    }

    /**
     * @param name         string id
     * @param requirements {@link Requirements}
     * @param resources    {@link Resource}
     */
    public record GroupSpec(String name,
                            Requirements requirements,
                            Collection<Resource> resources) {
    }

    /**
     * @param scope ??
     * @param xid   ??
     */
    public record Id(String scope, String xid) {
    }

    /**
     * Used to describe memory quantities
     *
     * @param quantity   {@link Quantity}
     * @param attributes {@link Attribute}
     */
    public record Memory(Quantity quantity,
                         Collection<Attribute> attributes) {
    }

    /**
     * Value to be used in a quantity
     *
     * @param val the value itself to be used as quantity
     */
    public record Quantity(String val) {

    }

    /**
     * Requirements used in the group spec
     *
     * @param signedBy   {@link SignedBy}
     * @param attributes {@link Attribute}
     */
    public record Requirements(@JsonProperty("signed_by")
                               SignedBy signedBy,
                               Collection<Attribute> attributes) {

    }

    /**
     * Quantities of resources asked in a deployment
     *
     * @param cpu       {@link Cpu}
     * @param memory    {@link Memory}
     * @param storage   {@link Storage}
     * @param endpoints {@link Endpoint}
     */
    public record DeploymentResource(
            Cpu cpu,
            Memory memory,
            Collection<Storage> storage,
            Collection<Endpoint> endpoints
    ) {

    }

    /**
     * Deployment resource
     *
     * @param resources {@link DeploymentResource}
     * @param count     how many
     * @param price     {@link Price} the cost
     */
    public record Resource(
            DeploymentResource resources,
            int count,
            Price price
    ) {
    }

    /**
     * Signature
     *
     * @param allOf ??
     * @param anyOf ??
     */
    public record SignedBy(@JsonProperty("all_of")
                           Collection<String> allOf,
                           @JsonProperty("any_of")
                           Collection<String> anyOf) {

    }

    /**
     * Describes deployment requested storage, if any
     *
     * @param name       requested storage name
     * @param quantity   {@link Quantity}
     * @param attributes {@link Attribute}
     */
    public record Storage(String name,
                          Quantity quantity,
                          Collection<Attribute> attributes) {
    }

    /**
     * Amount disbursed from account via payments.
     *
     * @param denom  denomination
     * @param amount how much
     */
    public record Transferred(
            String denom,
            String amount
    ) {

    }

    /**
     * Unit
     *
     * @param val actual value
     */
    public record Units(String val) {

    }

}
