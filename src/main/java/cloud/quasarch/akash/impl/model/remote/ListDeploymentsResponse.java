package cloud.quasarch.akash.impl.model.remote;

/**
 * POJO for Deployment information.
 */

public record ListDeploymentsResponse(
        Iterable<Deployment> deployments,
        PageInfo pagination
) {





}




