package org.quasarch.akash.model.remote;

/**
 * POJO for Deployment information.
 */

public record ListDeploymentsResponse(
        Iterable<Deployment> deployments,
        PageInfo pagination
) {





}




