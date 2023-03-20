package org.quasarch.akash.model.remote;

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
     * @param denom  todo dunno
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

    public record Funds(String denom, String amount) {
    }

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

    public record GroupId(
            String owner,
            @JsonProperty("dseq")
            String deploymentSequence,
            @JsonProperty("gseq")
            int groupSequence
    ) {
    }

    public record GroupSpec(String name,
                            Requirements requirements,
                            Collection<Resource> resources) {
    }

    public record Id(String scope, String xid) {
    }

    public record Memory(Quantity quantity,
                         Collection<Attribute> attributes) {
    }

    public record Quantity(String val) {

    }

    public record Requirements(@JsonProperty("signed_by")
                               SignedBy signedBy,
                               Collection<Attribute> attributes) {

    }

    public record DeploymentResource(
            Cpu cpu,
            Memory memory,
            Collection<Storage> storage,
            Collection<Endpoint> endpoints
    ) {

    }

    public record Resource(
            DeploymentResource resources,
            int count,
            Price price
    ) {


    }


    public record SignedBy(@JsonProperty("all_of")
                           Collection<String> allOf,
                           @JsonProperty("any_of")
                           Collection<String> anyOf) {

    }

    public record Storage(String name,
                          Quantity quantity,
                          Collection<Attribute> attributes) {
    }

    /**
     * Amount disbursed from account via payments.
     *
     * @param denom
     * @param amount
     */
    public record Transferred(
            String denom,
            String amount
    ) {

    }

    public record Units(String val) {

    }

}
