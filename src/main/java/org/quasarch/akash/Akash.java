package org.quasarch.akash;

import io.vavr.control.Either;
import org.jetbrains.annotations.Nullable;
import org.quasarch.akash.model.AkashPage;
import org.quasarch.akash.model.Bid;
import org.quasarch.akash.model.Deployment;
import org.quasarch.akash.model.DeploymentLease;
import org.quasarch.akash.model.OperationFailure;

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
     *
     * @param page               page number, 0 based index. Defaults to 0
     * @param resultPerPage      how many results per page. max is ??. Default to 10
     * @param deploymentSequence Deployment sequence number
     * @return Either {@link OperationFailure} or - if successful - {@link AkashPage<Deployment>}
     * @implSpec Request through REST.
     */
    Either<OperationFailure, AkashPage<Deployment>> listDeployments(@Nullable Short page,
                                                                    @Nullable Short resultPerPage,
                                                                    String deploymentSequence);

    /**
     * @param deploymentSequence Deployment sequence number
     * @return Either {@link OperationFailure} or - if successful - {@link Iterable<Deployment>}
     */
    Either<OperationFailure, Iterable<Deployment>> listDeployments(String deploymentSequence);

    /**
     * Fetches deployment info for the given deployment id
     *
     * @param owner ??
     * @param dSeq  ??
     * @return Either {@link OperationFailure} or - if successful - {@link Deployment}
     * @implSpec Request through REST.
     */
    Either<OperationFailure, Deployment> getDeployment(String owner, String dSeq);

    /**
     * Get the list of bids based on a set of filters.
     *
     * @param deploymentSequence
     * @param groupSequence      0 to x
     * @param oSeq               ??
     * @param providerId         identification of the provider
     * @param state              ??
     * @return A failure represented by {@link OperationFailure} or an {@link AkashPage<Bid>}
     * @implSpec Request through REST.
     */
    Either<OperationFailure, AkashPage<Bid>> listBids(String deploymentSequence, String groupSequence,
                                                      String oSeq, String providerId, String state,
                                                      short page, short resultsPerPage);

    /**
     * Get the list of bids based on a set of filters.
     *
     * @param deploymentSequence The deployment sequence
     * @param groupSequence      ?? -> 0 to x
     * @param oSeq               ??
     * @param providerId         identification of the provider
     * @param state              ??
     * @return Either {@link OperationFailure} or - if successful - an {@link Iterable<Bid>}
     * @implSpec Request through REST.
     */
    Either<OperationFailure, Iterable<Bid>> listBids(String deploymentSequence, String groupSequence,
                                                     String oSeq, String providerId, String state);

    /**
     * Get lease	Get information regarding a lease such as its status.
     *
     * @param deploymentSequence The deployment sequence
     * @param groupSequence      ?? -> 0 to x
     * @param oSeq               ??
     * @return Either {@link OperationFailure} or - if successful - {@link DeploymentLease}
     */
    Either<OperationFailure, DeploymentLease> getLease(String deploymentSequence,
                                                       String groupSequence,
                                                       String oSeq);

    /**
     *
     * Missing methods:
     * R_06	Create lease	Request through RPC.	1
     * R_07	 Request through REST.	1
     * R_08	Send manifest	Request through RPC.
     */


}
