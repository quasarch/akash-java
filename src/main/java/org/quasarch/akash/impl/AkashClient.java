package org.quasarch.akash.impl;

import io.vavr.control.Either;
import org.jetbrains.annotations.Nullable;
import org.quasarch.akash.Akash;
import org.quasarch.akash.impl.pagination.AkashPagedIterable;
import org.quasarch.akash.impl.parsing.ResponseParser;
import org.quasarch.akash.impl.parsing.ResponseParserBuilder;
import org.quasarch.akash.model.AkashPagedResponse;
import org.quasarch.akash.model.remote.Bid;
import org.quasarch.akash.model.remote.DeploymentLease;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.model.remote.Deployment;
import org.quasarch.akash.model.remote.ListBidResponse;
import org.quasarch.akash.model.remote.ListDeploymentLeaseResponse;
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

        log.debug("listDeployments called for owner {}, state {} and deploymentSequence {}",
                owner,
                state,
                deploymentSequence);

        var requestUri = addQueryParameters(
                URI.create(baseUri + DEPLOYMENT_LIST_URI),
                new QueryParam("filters.owner", owner),
                new QueryParam("filters.dseq", deploymentSequence),
                new QueryParam("filters.state", state)
        );
        log.debug("using {} path to list deployments", requestUri);
        var responseParser = ResponseParserBuilder
                .<AkashPagedResponse<Deployment>, ListDeploymentsResponse>newBuilder()
                .withResultClass(ListDeploymentsResponse.class)
                .withIntermediateOperation(
                        intermediate -> new AkashPagedResponse<>(intermediate.deployments(),
                                intermediate.pagination())
                )
                .build();
        return listRequest(requestUri, responseParser).map(fPage -> new AkashPagedIterable<>(
                nextPage -> listRequest(addQueryParameters(requestUri,
                        new QueryParam("pagination.key", nextPage)
                ), responseParser).get(),
                fPage
        ));

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
        var responseParser = ResponseParserBuilder
                .<Deployment, Deployment>newBuilder()
                .withResultClass(Deployment.class)
                .build();
        return getRequest(requestUri, responseParser);

    }

    @Override
    public Either<OperationFailure, Iterable<Bid>> listBids(

            @Nullable String owner,
            @Nullable String deploymentSequence,
            @Nullable String groupSequence,
            @Nullable String oSeq,
            @Nullable String providerId,
            @Nullable String state) {
        log.debug("listBids called with filters:  " +
                        "owner {}, deploymentSequence {}, groupSequence {} and oSeq {}" +
                        "providerId {}, state {}",
                deploymentSequence,
                groupSequence,
                oSeq,
                providerId,
                state);

        var requestUri = addQueryParameters(
                URI.create(baseUri + BID_LIST_URI),
                new QueryParam("filters.owner", owner),
                new QueryParam("filters.dseq", deploymentSequence),
                new QueryParam("filters.gseq", state),
                new QueryParam("filters.oseq", state),
                new QueryParam("filters.provider", state),
                new QueryParam("filters.state", state)
                //new QueryParam("pagination.key", nextPageKey)
        );
        log.debug("using {} path to list bids", requestUri);
        var responseParser = ResponseParserBuilder
                .<AkashPagedResponse<Bid>, ListBidResponse>newBuilder()
                .withResultClass(ListBidResponse.class)
                .withIntermediateOperation(
                        intermediate -> new AkashPagedResponse<>(intermediate.bids(),
                                intermediate.pagination())
                )
                .build();
        return listRequest(requestUri, responseParser)
                .map(fPage -> new AkashPagedIterable<>(
                        nextPage -> listRequest(addQueryParameters(requestUri,
                                new QueryParam("pagination.key", nextPage)
                        ), responseParser).get(),
                        fPage
                ));

    }

    @Override
    public Either<OperationFailure, Bid> getBid(String owner, String deploymentSequence,
                                                @Nullable String groupSequence,
                                                @Nullable String orderSequence,
                                                String providerId) {
        log.debug("getBid called for owner {} " +
                        "deploymentSequence {} " +
                        "groupSequence {} " +
                        "orderSequence {}" +
                        "and providerId {}",
                owner,
                deploymentSequence, groupSequence, orderSequence, providerId);
        Objects.requireNonNull(owner);
        Objects.requireNonNull(deploymentSequence);
        Objects.requireNonNull(providerId);

        groupSequence = defaultsTo(groupSequence, () -> "1");
        orderSequence =
                defaultsTo(orderSequence, () -> "1");

        var requestUri = addQueryParameters(
                URI.create(baseUri + BID_GET_URI),
                new QueryParam("id.owner", owner),
                new QueryParam("id.dseq", deploymentSequence),
                new QueryParam("id.gseq", groupSequence),
                new QueryParam("id.oseq", orderSequence),
                new QueryParam("id.provider", providerId)
        );

        var responseParser = ResponseParserBuilder
                .<Bid, Bid>newBuilder()
                .withResultClass(Bid.class)
                .build();
        return getRequest(requestUri, responseParser);


    }

    @Override
    public Either<OperationFailure, DeploymentLease> getLease(
            String owner,
            String deploymentSequence,
            String groupSequence,
            String orderSequence,
            String provider
    ) {
        log.debug("getLease called for owner {} " +
                        "deploymentSequence {} " +
                        "groupSequence {} " +
                        "orderSequence {}" +
                        "and provider {}",
                owner,
                deploymentSequence,
                groupSequence,
                orderSequence,
                provider);

        groupSequence = defaultsTo(groupSequence, () -> "1");
        orderSequence =
                defaultsTo(orderSequence, () -> "1");

        Objects.requireNonNull(owner);
        Objects.requireNonNull(deploymentSequence);
        Objects.requireNonNull(groupSequence);
        Objects.requireNonNull(orderSequence);
        Objects.requireNonNull(provider);

        var requestUri = addQueryParameters(
                URI.create(baseUri + LEASE_GET_URI),
                new QueryParam("id.owner", owner),
                new QueryParam("id.dseq", deploymentSequence),
                new QueryParam("id.gseq", groupSequence),
                new QueryParam("id.oseq", orderSequence),
                new QueryParam("id.provider", provider)
        );
        log.debug("using {} path to get lease", requestUri);

        var request = HttpRequest
                .newBuilder(requestUri)
                .GET()
                .build();

        var responseParser = ResponseParserBuilder
                .<DeploymentLease, DeploymentLease>newBuilder()
                .withResultClass(DeploymentLease.class)
                .build();

        var bodyFuture = httpClientSupplier.get()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(responseParser::parseToEither);

        return extractEither(bodyFuture);
    }

    @Override
    public Either<OperationFailure, Iterable<DeploymentLease>> listLeases(@Nullable String owner,
                                                                          @Nullable String deploymentSequence,
                                                                          @Nullable String groupSequence,
                                                                          @Nullable String orderSequence,
                                                                          @Nullable String provider,
                                                                          @Nullable String state) {
        log.debug("listBids called with filters:  " +
                        "owner {}, deploymentSequence {}, groupSequence {} and orderSequence {}" +
                        "providerId {}, state {}",
                owner,
                deploymentSequence,
                groupSequence,
                orderSequence,
                provider,
                state);

        var requestUri = addQueryParameters(
                URI.create(baseUri + LEASE_LIST_URI),
                new QueryParam("filters.owner", owner),
                new QueryParam("filters.dseq", deploymentSequence),
                new QueryParam("filters.gseq", state),
                new QueryParam("filters.oseq", state),
                new QueryParam("filters.provider", state),
                new QueryParam("filters.state", state)
        );
        log.debug("using {} path to list leases", requestUri);
        var responseParser = ResponseParserBuilder
                .<AkashPagedResponse<DeploymentLease>, ListDeploymentLeaseResponse>newBuilder()
                .withResultClass(ListDeploymentLeaseResponse.class)
                .withIntermediateOperation(
                        intermediate -> new AkashPagedResponse<>(intermediate.leases(),
                                intermediate.pagination())
                )
                .build();
        return listRequest(requestUri, responseParser)
                .map(fPage -> new AkashPagedIterable<>(
                        nextPage -> listRequest(addQueryParameters(requestUri,
                                new QueryParam("pagination.key", nextPage)
                        ), responseParser).get(),
                        fPage
                ));
    }

    /**
     * for internal use
     * Helps to to a request of / list type
     *
     * @param requestUri     uri with query params already added
     * @param responseParser
     * @param <T>
     * @return
     */
    private <T> Either<OperationFailure, AkashPagedResponse<T>> listRequest(
            URI requestUri,
            ResponseParser<AkashPagedResponse<T>> responseParser
    ) {
        log.error("listRequest called for requestUri {}", requestUri);
        var request = HttpRequest
                .newBuilder(requestUri)
                .GET()
                .build();

        var bodyFuture = httpClientSupplier.get()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(responseParser::parseToEither);

        return extractEither(bodyFuture);
    }

    private <T> Either<OperationFailure, T> getRequest(
            URI requestUri,
            ResponseParser<T> responseParser
    ) {
        var request = HttpRequest
                .newBuilder(requestUri)
                .GET()
                .build();

        var bodyFuture = httpClientSupplier.get()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(responseParser::parseToEither);

        return extractEither(bodyFuture);

    }


    private static <T> T defaultsTo(T instance, Supplier<T> defaultOnNull) {
        if (instance == null)
            instance = defaultOnNull.get();
        return instance;
    }

    private static <T> Either<OperationFailure, T> extractEither(CompletableFuture<Either<OperationFailure, T>> future) {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            return Either.left(new OperationFailure(ClientError, ex.getMessage()));
        }


    }

    private static final String DEPLOYMENT_URI = "/api/akash/deployment/v1beta2/deployments/";
    private static final String DEPLOYMENT_LIST_URI = DEPLOYMENT_URI + "list";
    private static final String DEPLOYMENT_GET_URI = DEPLOYMENT_URI + "info";

    private static final String BID_URI = "/api/akash/market/v1beta2/bids/";
    private static final String BID_GET_URI = BID_URI + "info";
    private static final String BID_LIST_URI = BID_URI + "list";

    private static final String LEASE_URI = "/api/akash/market/v1beta2/leases/";
    private static final String LEASE_GET_URI = LEASE_URI + "info";
    private static final String LEASE_LIST_URI = LEASE_URI + "list";


}
