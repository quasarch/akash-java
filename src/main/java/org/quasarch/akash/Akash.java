package org.quasarch.akash;

import io.vavr.control.Either;
import org.quasarch.akash.model.DeploymentInfo;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.OperationResult;

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
 *
 * Open questions: Should we really use Either in this interface?
 */
public interface Akash {

    /**
     * Create a deployment based on an SDL file. Request through RPC.
     * @param sdlFile Path to the template file
     * @return Either, right biased
     */
    Either<OperationFailure, DeploymentInfo> createDeployment(Path sdlFile);

    /**
     * OPEN QUESTION: LEASE ID OR DEPLOYMENT_ID?
     *
     * Close a deployment. Request through RPC
     * @param leaseId The id of the lease which will be terminated
     * @return DeploymentInfo
     */
    Either<OperationFailure, DeploymentInfo> closedDeployment(String leaseId);

    /**
     * R_03	List deployments	List all deployments matching the filters with consideration for page limits. Request through REST.	1
     * R_04	Get deployment	Request through REST.	1
     * R_05	List bids	Get the list of bids based on a set of filters. Request through REST.	1
     * R_06	Create lease	Request through RPC.	1
     * R_07	Get lease	Get information regarding a lease such as its status. Request through REST.	1
     * R_08	Send manifest	Request through RPC.
     */


}
