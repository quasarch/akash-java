package org.quasarch.akash.model.remote;

public record ListDeploymentLeaseResponse(
        Iterable<DeploymentLease> leases,
        PageInfo pagination) {
}
