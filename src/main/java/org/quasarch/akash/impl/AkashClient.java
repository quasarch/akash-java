package org.quasarch.akash.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.quasarch.akash.Akash;
import org.quasarch.akash.impl.pagination.AkashPagedIterable;
import org.quasarch.akash.model.AkashPagedResponse;
import org.quasarch.akash.model.Bid;
import org.quasarch.akash.model.DeploymentLease;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.PagedResponse;
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

import static org.quasarch.akash.uri.UriUtils.addQueryParameters;

/**
 * Implementation of {@link Akash}.
 */
public final class AkashClient implements Akash {
    private final String accountAddress;
    private final URI baseUri;
    private final Supplier<HttpClient> httpClientSupplier;
    private static final Logger log = LoggerFactory.getLogger(AkashClient.class);
    private static final String baseUriEnvVariableKey = "QUASARCH_AKASH_BASE_URI";

    /**
     * Uses QUASARCH_AKASH_BASE_URI env variable to get baseUri address
     *
     * @param accountAddress
     */
    public AkashClient(String accountAddress) {
        var baseUri = System.getenv(baseUriEnvVariableKey);
        Objects.requireNonNull(baseUri);
        this.baseUri = URI.create(baseUri);
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
    public Either<OperationFailure, PagedResponse<Deployment>> listDeployments(Short page,
                                                                               Short resultPerPage,
                                                                               String deploymentSequence) {
        Objects.requireNonNull(deploymentSequence, "deploymentSequence cannot be empty on listDeployments");
        defaultsTo(page, () -> 0L);
        defaultsTo(resultPerPage, () -> 10L);


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
                URI.create(baseUri + listDeploymentUri),
                new QueryParam("filters.owner", owner),
                new QueryParam("filters.dseq", deploymentSequence),
                // TODO what is state?
                new QueryParam("filters.state", null),
                new QueryParam("pagination.key", nextPageKey)
        );
        log.debug("using {} path to list deployments", requestUri);


        var request = HttpRequest
                .newBuilder(requestUri)
                .GET()
                .build();

        var bodyFuture = httpClientSupplier.get()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(AkashClient::fromBody);

        // ignore pagination, we didnt ask for it
        return getEither(bodyFuture);

    }

    @Override
    public Either<OperationFailure, Iterable<Deployment>> listDeployments(
            String owner,
            String state,
            String deploymentSequence
    ) {

        // ignore pagination, we didnt ask for it
        return listDeployments(owner, state, deploymentSequence, null)
                .map(firstPage -> new AkashPagedIterable<>(
                        (nextToken) -> listDeployments(owner, state, deploymentSequence, nextToken).get(),
                        firstPage));


    }

    @Override
    public Either<OperationFailure, Iterable<Deployment>> listMyDeployments(String state, String deploymentSequence) {
        return listDeployments(accountAddress, state, deploymentSequence);
    }

    @Override
    public Either<OperationFailure, Deployment> getDeployment(String owner, String dSeq) {
        return null;
    }

    @Override
    public Either<OperationFailure, PagedResponse<Bid>> listBids(String deploymentSequence, String groupSequence, String oSeq, String providerId, String state, short page, short resultsPerPage) {
        return null;
    }

    @Override
    public Either<OperationFailure, Iterable<Bid>> listBids(String deploymentSequence, String groupSequence, String oSeq, String providerId, String state) {
        return null;
    }

    @Override
    public Either<OperationFailure, DeploymentLease> getLease(String deploymentSequence, String groupSequence, String oSeq) {
        return null;
    }

    private static <T> void defaultsTo(T instance, Supplier<T> defaultOnNull) {
        if (instance == null)
            instance = defaultOnNull.get();
    }

    private static Either<OperationFailure, AkashPagedResponse<Deployment>> fromBody(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        // todo move this jackson specifics to global place
        //objectMapper.registerModule(new JavaTimeModule());
        try {
            log.trace("received response string: {}", body);
            var deploymentResponse = objectMapper.readValue(body, ListDeploymentsResponse.class);

            return Either.right(new AkashPagedResponse<>(deploymentResponse.deployments(),
                    deploymentResponse.pagination()));
        } catch (JsonProcessingException jsonEx) {
            return Either.left(OperationFailure.from(jsonEx));
        }
    }

    private static <T> Either<OperationFailure, T> getEither(CompletableFuture<Either<OperationFailure, T>> future) {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException ex) {
            return Either.left(OperationFailure.from(ex));
        }
    }

    private final static String listDeploymentUri = "/api/akash/deployment/v1beta2/deployments/list";
}
