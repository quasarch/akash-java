package cloud.quasarch.akash.impl.model.remote;

/**
 * Response wrapper
 *
 * @param leases     {@link DeploymentLease}
 * @param pagination {@link PageInfo}
 */
public record ListDeploymentLeaseResponse(
        Iterable<DeploymentLease> leases,
        PageInfo pagination) {
}
