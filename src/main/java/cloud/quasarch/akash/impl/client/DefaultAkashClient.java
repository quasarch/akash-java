package cloud.quasarch.akash.impl.client;

import cloud.quasarch.akash.AkashClient;
import cloud.quasarch.akash.impl.model.AkashPagedResponse;
import cloud.quasarch.akash.impl.model.OperationFailure;
import cloud.quasarch.akash.impl.model.remote.*;
import cloud.quasarch.akash.impl.pagination.AkashPagedIterable;
import cloud.quasarch.akash.impl.parsing.ResponseParser;
import cloud.quasarch.akash.impl.parsing.ResponseParserBuilder;
import cloud.quasarch.akash.impl.uri.ApiEndpoints;
import cloud.quasarch.akash.impl.uri.QueryParam;
import io.vavr.control.Either;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shaded_package.org.apache.commons.lang3.NotImplementedException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static cloud.quasarch.akash.impl.model.remote.AkashErrorType.ClientError;
import static cloud.quasarch.akash.impl.uri.UriUtils.addQueryParameters;

/**
 * Implementation of {@link AkashClient}.
 */
final class DefaultAkashClient implements AkashClient {

    private static final Logger log = LoggerFactory.getLogger(DefaultAkashClient.class);

    private final String accountAddress;
    private final URI apiBaseUri;
    private HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Constructor for {@link DefaultAkashClient}
     *
     * @param accountAddress     the address for the akt account
     * @param apiBaseUri         base uri for the api
     * @param httpClient supplier of {@link HttpClient}
     */
    DefaultAkashClient(String accountAddress, URI apiBaseUri, HttpClient httpClient) {
        this.accountAddress = accountAddress;
        this.apiBaseUri = apiBaseUri;
        Objects.requireNonNull(httpClient);
        this.httpClient = httpClient;
    }

    /**
     * Constructor for {@link DefaultAkashClient}
     *
     * @param accountAddress address to be used when required.
     * @param apiBaseUri base uri for the api
     */
    DefaultAkashClient(String accountAddress, URI apiBaseUri) {
        this.accountAddress = accountAddress;
        this.apiBaseUri = apiBaseUri;
    }

    @Override
    public Either<OperationFailure, Deployment> createDeployment(Path sdlFile) {
        Objects.requireNonNull(sdlFile, "sdlFile cannot be empty on createDeployment");
        return null;
    }

    @Override
    public Either<OperationFailure, Deployment> closedDeployment(String leaseId) {
        throw new NotImplementedException("");
    }

    @Override
    public Either<OperationFailure, DeploymentLease> createLease() {
        throw new NotImplementedException("");
    }

    @Override
    public Either<OperationFailure, ?> sendManifest(Path sdlFile) {
        throw new NotImplementedException("");
    }

    @Override
    public Either<OperationFailure, ?> updateManifest(Path sdlFile) {
        throw new NotImplementedException("");
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
                URI.create(apiBaseUri + ApiEndpoints.Deployments.LIST),
                new QueryParam(FILTERS_OWNER, owner),
                new QueryParam(FILTERS_DSEQ, deploymentSequence),
                new QueryParam(FILTERS_STATE, state)
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
                        new QueryParam(PAGINATION_KEY, nextPage)
                ), responseParser).get(),
                fPage
        ));

    }


    /**
     * @param owner              ??
     * @param deploymentSequence ??
     * @return Either {@link Deployment} or {@link OperationFailure} on failure
     * @see AkashClient#getDeployment(String, String)
     */
    @Override
    public Either<OperationFailure, Deployment> getDeployment(String owner, String deploymentSequence) {
        log.debug("getDeployment called for owner {} and deploymentSequence {}",
                owner,
                deploymentSequence);
        Objects.requireNonNull(owner);
        Objects.requireNonNull(deploymentSequence);

        var requestUri = addQueryParameters(
                URI.create(apiBaseUri + ApiEndpoints.Deployments.INFO),
                new QueryParam(ID_OWNER, owner),
                new QueryParam(ID_DSEQ, deploymentSequence)
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
                owner,
                deploymentSequence,
                groupSequence,
                oSeq,
                providerId,
                state);

        var requestUri = addQueryParameters(
                URI.create(apiBaseUri + ApiEndpoints.Bids.LIST),
                new QueryParam(FILTERS_OWNER, owner),
                new QueryParam(FILTERS_DSEQ, deploymentSequence),
                new QueryParam("filters.gseq", state),
                new QueryParam("filters.oseq", state),
                new QueryParam("filters.provider", state),
                new QueryParam(FILTERS_STATE, state)
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
                                new QueryParam(PAGINATION_KEY, nextPage)
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
                URI.create(apiBaseUri + ApiEndpoints.Bids.INFO),
                new QueryParam(ID_OWNER, owner),
                new QueryParam(ID_DSEQ, deploymentSequence),
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
                URI.create(apiBaseUri + ApiEndpoints.Leases.INFO),
                new QueryParam(ID_OWNER, owner),
                new QueryParam(ID_DSEQ, deploymentSequence),
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

        var bodyFuture = httpClient
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
                URI.create(apiBaseUri + ApiEndpoints.Leases.LIST),
                new QueryParam(FILTERS_OWNER, owner),
                new QueryParam(FILTERS_DSEQ, deploymentSequence),
                new QueryParam("filters.gseq", state),
                new QueryParam("filters.oseq", state),
                new QueryParam("filters.provider", state),
                new QueryParam(FILTERS_STATE, state)
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
                                new QueryParam(PAGINATION_KEY, nextPage)
                        ), responseParser).get(),
                        fPage
                ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultAkashClient that = (DefaultAkashClient) o;
        return Objects.equals(accountAddress, that.accountAddress) && Objects.equals(apiBaseUri, that.apiBaseUri) && Objects.equals(httpClient, that.httpClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountAddress, apiBaseUri, httpClient);
    }

    @Override
    public String toString() {
        return "DefaultAkashClient{" +
               "accountAddress='" + accountAddress + '\'' +
               ", apiBaseUri=" + apiBaseUri +
               ", httpClient=" + httpClient +
               '}';
    }

    /**
     * for internal use
     * Helps to to a request of / list type
     *
     * @param requestUri     uri with query params already added
     * @param responseParser parses AkashPagedResponse
     * @param <T>            Type of response.
     * @return EIther a Paged reponse of T or a {@link OperationFailure}
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

        var bodyFuture = httpClient
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

        var bodyFuture = httpClient
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

    private static final String FILTERS_OWNER = "filters.owner";
    private static final String FILTERS_DSEQ = "filters.dseq";
    private static final String FILTERS_STATE = "filters.state";
    private static final String PAGINATION_KEY = "pagination.key";
    private static final String ID_OWNER = "id.owner";
    private static final String ID_DSEQ = "id.dseq";
}
