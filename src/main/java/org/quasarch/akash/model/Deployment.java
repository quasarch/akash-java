package org.quasarch.akash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

/**
 * POJO for Deployment information.
 */
public record Deployment(
        DeploymentInfo deployment,
        Collection<Group> groups,
        EscrowAccount escrow_account
) {

    public record DeploymentInfo(
            @JsonProperty("deployment_id")
            DeploymentId deploymentId,
            String state,
            String version,
            @JsonProperty("created_at")
            Date createdAt) {
    }

    public record Attribute(String key,
                            String value) {
    }

    public record Balance(String denom,
                          String amount
    ) {
    }

    public record Cpu(Units units,
                      Collection<Attribute> attributes) {

    }

    public record DeploymentId(
            String owner,
            @JsonProperty("dseq")
            String deploymentSequence
    ) {
    }

    public record Endpoint(
            String kind,
            @JsonProperty("sequence_number")
            int sequenceNumber
    ) {
    }

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
            @JsonProperty("created_at")
            LocalDate createdAt
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

    public record Price(
            String denom,
            String amount
    ) {

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

    public record Transferred(
            String denom,
            String amount
    ) {

    }

    public record Units(String val) {

    }

}




