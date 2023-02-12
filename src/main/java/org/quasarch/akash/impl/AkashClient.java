package org.quasarch.akash.impl;

import io.vavr.control.Either;
import org.quasarch.akash.Akash;
import org.quasarch.akash.impl.pagination.AkashPagedIterable;
import org.quasarch.akash.impl.parsing.ResponseParserBuilder;
import org.quasarch.akash.model.AkashPagedResponse;
import org.quasarch.akash.model.Bid;
import org.quasarch.akash.model.DeploymentLease;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.remote.Deployment;
import org.quasarch.akash.model.remote.ListDeploymentsResponse;
import org.quasarch.akash.uri.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.quasarch.akash.model.remote.AkashErrorType.ClientError;
import static org.quasarch.akash.uri.UriUtils.addQueryParameters;

/**
 * Implementation of {@link Akash}.
 */
public final class AkashClient implements Akash {
    private final String accountAddress;
    private final URI baseUri;
    private final Supplier<HttpClient> httpClientSupplier;
    private static final Logger log = LoggerFactory.getLogger(AkashClient.class);
    private static final String BASE_URI_ENV_VARIABLE_KEY = "QUASARCH_AKASH_BASE_URI";

    /**
     * Uses QUASARCH_AKASH_BASE_URI env variable to get baseUri address
     *
     * @param accountAddress address to be used when required.
     */
    public AkashClient(String accountAddress) {
        var envBaseUri = System.getenv(BASE_URI_ENV_VARIABLE_KEY);
        Objects.requireNonNull(envBaseUri);
        this.baseUri = URI.create(envBaseUri);
        this.accountAddress = accountAddress;
        // reuse same http client instance
        final var httpClient = HttpClient.newHttpClient();
        this.httpClientSupplier = () -> httpClient;
    }

    public AkashClient(String accountAddress, URI baseUri, Supplier<HttpClient> httpClientSupplier) {
        this.accountAddress = accountAddress;
        this.baseUri = baseUri;
        this.httpClientSupplier = httpClientSupplier;
    }

    @Override
    public Either<OperationFailure, Deployment> createDeployment(Path sdlFile) {
        Objects.requireNonNull(sdlFile, "sdlFile cannot be empty on createDeployment");
        return null;
    }

    @Override
    public Either<OperationFailure, Deployment> closedDeployment(String leaseId) {
        return null;
    }


    @Override
    public Either<OperationFailure, Iterable<Deployment>> listDeployments(
            String owner,
            String state,
            String deploymentSequence
    ) {
        return listDeployments(owner, state, deploymentSequence, null)
                .map(firstPage -> new AkashPagedIterable<>(
                        nextToken -> listDeployments(owner, state, deploymentSequence, nextToken).get(),
                        firstPage));
    }


    /**
     * @param owner              ??
     * @param deploymentSequence ??
     * @return
     * @see Akash#getDeployment(String, String)
     */
    @Override
    public Either<OperationFailure, Deployment> getDeployment(String owner, String deploymentSequence) {
        log.debug("getDeployment called for owner {} and deploymentSequence {}",
                owner,
                deploymentSequence);
        Objects.requireNonNull(owner);
        Objects.requireNonNull(deploymentSequence);

        var requestUri = addQueryParameters(
                URI.create(baseUri + DEPLOYMENT_GET_URI),
                new QueryParam("id.owner", owner),
                new QueryParam("id.dseq", deploymentSequence)
        );
        log.debug("using {} path to get deployment", requestUri);

        var request = HttpRequest
                .newBuilder(requestUri)
                .GET()
                .build();

        var responseParser = ResponseParserBuilder
                .<Deployment, Deployment>newBuilder()
                .withResultClass(Deployment.class)
                .build();

        var bodyFuture = httpClientSupplier.get()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(responseParser::parseToEither);

        return extractEither(bodyFuture);

    }

    @Override
    public Either<OperationFailure, Iterable<Bid>> listBids(String deploymentSequence, String groupSequence, String oSeq, String providerId, String state) {
        return null;
    }

    @Override
    public Either<OperationFailure, DeploymentLease> getLease(String deploymentSequence, String groupSequence, String oSeq) {
        return null;
    }


    private Either<OperationFailure, AkashPagedResponse<Deployment>> listDeployments(
            String owner,
            String state,
            String deploymentSequence,
            String nextPageKey
    ) {
        log.debug("listDeployments called for owner {}, state {} and deploymentSequence {}",
                owner,
                state,
                deploymentSequence);

        var requestUri = addQueryParameters(
                URI.create(baseUri + DEPLOYMENT_LIST_URI),
                new QueryParam("filters.owner", owner),
                new QueryParam("filters.dseq", deploymentSequence),
                new QueryParam("filters.state", state),
                new QueryParam("pagination.key", nextPageKey)
        );
        log.debug("using {} path to list deployments", requestUri);


        var request = HttpRequest
                .newBuilder(requestUri)
                .GET()
                .build();

        var responseParser = ResponseParserBuilder
                .<AkashPagedResponse<Deployment>, ListDeploymentsResponse>newBuilder()
                .withResultClass(ListDeploymentsResponse.class)
                .withIntermediateOperation(
                        intermediate -> new AkashPagedResponse<>(intermediate.deployments(),
                                intermediate.pagination())
                )
                .build();

        var bodyFuture = httpClientSupplier.get()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(responseParser::parseToEither);

        return extractEither(bodyFuture);

    }


    private static <T> void defaultsTo(T instance, Supplier<T> defaultOnNull) {
        if (instance == null)
            instance = defaultOnNull.get();
    }

    private static <T> Either<OperationFailure, T> extractEither(CompletableFuture<Either<OperationFailure, T>> future) {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            return Either.left(new OperationFailure(ClientError, ex.getMessage()));
        }
    }

    private static final String DEPLOYMENT_LIST_URI = "/api/akash/deployment/v1beta2/deployments/list";
    private static final String DEPLOYMENT_GET_URI = "/api/akash/deployment/v1beta2/deployments/info";


}
