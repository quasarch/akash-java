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
 * <p>
 * <p>
 * Open questions: Should we really use Either in this interface?
 */
public interface Akash {

    /**
     * Create a deployment based on an SDL file. Request through RPC.
     *
     * @param sdlFile Path to the template file
     *                * @return Either {@link OperationFailure} or - if successful - {@link Deployment}
     */
    Either<OperationFailure, Deployment> createDeployment(Path sdlFile);

    /**
     * OPEN QUESTION: LEASE ID OR DEPLOYMENT_ID?
     * <p>
     * Close a deployment.
     *
     * @param leaseId The id of the lease which will be terminated
     *                * @return Either {@link OperationFailure} or - if successful - {@link Deployment}
     * @implNote Request through RPC
     */
    Either<OperationFailure, Deployment> closedDeployment(String leaseId);

    /**
     * List all deployments matching the filters with consideration for page limits.
     * Not implemented: This might not be needed at all since listDeployments() support pagination
     * using {@link org.quasarch.akash.impl.pagination.AkashPagedIterable}.
     * Kept for future iterations
     *
     * @param page               page number, 0 based index. Defaults to 0
     * @param resultPerPage      how many results per page. max is ??. Default to 10
     * @param deploymentSequence Deployment sequence number
     * @return Either {@link OperationFailure} or - if successful - {@link PagedResponse< Deployment >}
     * @implSpec Request through REST.
    Either<OperationFailure, PagedResponse<Deployment>> listDeployments(@Nullable Short page,
     @Nullable Short resultPerPage,
     String deploymentSequence);*/

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
     * Not implemented: This might not be needed at all since listDeployments() support pagination
     *      * using {@link org.quasarch.akash.impl.pagination.AkashPagedIterable}.
     *      * Kept for future iterations
     * @param deploymentSequence
     * @param groupSequence      0 to x
     * @param oSeq               ??
     * @param providerId         identification of the provider
     * @param state              ??
     * @return A failure represented by {@link OperationFailure} or an {@link PagedResponse<Bid>}
     * @implSpec Request through REST.

    Either<OperationFailure, PagedResponse<Bid>> listBids(String deploymentSequence, String groupSequence,
    String oSeq, String providerId, String state,
    short page, short resultsPerPage);
     */

    /**
     * Get the list of bids based on a set of filters.
     *
     * @param owner
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
     * @param providerId
     * @return
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
     * @param groupSequence
     * @param orderSequence
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
     * @param provider
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

    /**
     *
     * Missing methods:
     * R_06	Create lease	Request through RPC.	1
     * R_07	 Request through REST.	1
     * R_08	Send manifest	Request through RPC.
     */


}
