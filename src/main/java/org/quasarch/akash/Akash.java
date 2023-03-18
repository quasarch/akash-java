package org.quasarch.akash;

import io.vavr.control.Either;
import org.jetbrains.annotations.Nullable;
import org.quasarch.akash.model.PagedResponse;
import org.quasarch.akash.model.remote.Bid;

import org.quasarch.akash.model.remote.DeploymentLease;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.remote.Deployment;

import java.nio.file.Path;

/**
 * <p>
 * Akash is an open network that lets users buy and sell computing resources securely and efficiently.
 * Purpose-built for public utility.
 * </p>
 * <p>
 * This client supports operations highlighted in
 * <a href="https://github.com/akash-network/community/blob/main/sig-clients/client-libraries/prd.md">...</a>
 * </p>
 * Open questions: Should we really use Either in this interface?
 */
public interface Akash {

    /**
     * Create a deployment based on an SDL file. Request through RPC.
     *
     * @param sdlFile Path to the template file
     *                * @return Either {@link OperationFailure} or - if successful - {@link Deployment}
     * @return Either with {@link Deployment} or with 'left' failure containing {@link OperationFailure}
     */
    Either<OperationFailure, Deployment> createDeployment(Path sdlFile);

    /**
     * OPEN QUESTION: LEASE ID OR DEPLOYMENT_ID?
     * <p>
     * Close a deployment.
     *
     * @param leaseId The id of the lease which will be terminated
     *                * @return Either {@link OperationFailure} or - if successful - {@link Deployment}
     * @return Either with {@link Deployment} or with 'left' failure containing {@link OperationFailure}
     * @implNote Request through RPC
     */
    Either<OperationFailure, Deployment> closedDeployment(String leaseId);

    /**
     * R_06 Provides lease creation capabilities
     *
     * @return TBD
     */
    Either<OperationFailure, DeploymentLease> createLease();

    /**
     * TODO SPEC
     * R_08 Provides a way for a manifest/SDL file to be send. Part of the deployment process
     *
     * @param sdlFile the path for the deployment file descriptor
     * @return TBD
     */
    Either<OperationFailure, ?> sendManifest(Path sdlFile);

    /**
     * TODO SPEC
     * R_09 updates the descriptor of an already deployment
     *
     * @param sdlFile the path for the deployment file descriptor
     * @return TBD
     */
    Either<OperationFailure, ?> updateManifest(Path sdlFile);


    /**
     * List all the deployment for given ( optional ) filters.
     * Clients should know that the returned iterable might need to fetch more data from upstream.
     *
     * @param owner              The owner to filter for
     * @param state              Filter for deployments in state.
     * @param deploymentSequence Deployment sequence number. This is a filter, not required
     * @return Either {@link OperationFailure} or - if successful - {@link Iterable< Deployment >}
     */
    Either<OperationFailure, Iterable<Deployment>> listDeployments(
            @Nullable String owner,
            @Nullable String state,
            @Nullable String deploymentSequence);


    /**
     * Fetches deployment info for the given deployment id.
     *
     * @param owner              owner, mandatory
     * @param deploymentSequence Deployment sequence, mandatory
     * @return Either {@link OperationFailure} or - if successful - {@link Deployment}
     * @implSpec Request through REST.
     */
    Either<OperationFailure, Deployment> getDeployment(String owner, String deploymentSequence);


    /**
     * Get the list of bids based on a set of filters.
     *
     * @param owner              owner of the bid
     * @param deploymentSequence The deployment sequence
     * @param groupSequence      GSEQ is used to distinguish “groups” of containers in a deployment.
     *                           Each group can be leased independently -
     *                           orders, bids, and leases all act on a single group.
     * @param oSeq               Akash OSEQ is used to distinguish multiple orders associated with a single deployment.
     * @param providerId         identification of the provider
     * @param state              ??
     * @return Either {@link OperationFailure} or - if successful - an {@link Iterable<Bid>}
     * @implSpec Request through REST.
     */
    Either<OperationFailure, Iterable<Bid>> listBids(
            @Nullable String owner,
            @Nullable String deploymentSequence,
            @Nullable String groupSequence,
            @Nullable String oSeq,
            @Nullable String providerId,
            @Nullable String state);


    /**
     * @param owner              mandatory, the account of the bid requester
     * @param deploymentSequence mandatory, the deployment sequence
     * @param groupSequence      group sequence, defaults to 1 if not provided
     * @param orderSequence      order sequence, defaults to 1 if not provided
     * @param providerId         id of the provider where the bid was made
     * @return Either the {@link Bid} object or the {@link OperationFailure} on failure
     */
    Either<OperationFailure, Bid> getBid(
            String owner,
            String deploymentSequence,
            @Nullable String groupSequence,
            @Nullable String orderSequence,
            String providerId
    );

    /**
     * Get lease	Get information regarding a lease such as its status.
     *
     * @param deploymentSequence The deployment sequence
     * @param groupSequence      sequence which identifies deployment group
     * @param orderSequence      sequence of the deployment order
     * @param owner              the account address of the lease owner
     * @param provider           identification of the provider where the lease was made
     * @return Either {@link OperationFailure} or - if successful - {@link DeploymentLease}
     */
    Either<OperationFailure, DeploymentLease> getLease(
            String owner,
            String deploymentSequence,
            String groupSequence,
            String orderSequence,
            String provider
    );

    /**
     * List all the leases for a given filter set
     *
     * @param owner              the account address of the lease owner
     * @param deploymentSequence the deployment sequence
     * @param groupSequence      the group sequence
     * @param orderSequence      the order sequence
     * @param provider           identification of the provider where the lease was made
     * @param state              state can be one of ( completed, active, open )
     * @return Can be a {@link DeploymentLease} iterable, or a failure
     */
    Either<OperationFailure, Iterable<DeploymentLease>> listLeases(
            @Nullable String owner,
            @Nullable String deploymentSequence,
            @Nullable String groupSequence,
            @Nullable String orderSequence,
            @Nullable String provider,
            @Nullable String state
    );


}
