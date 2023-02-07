package org.quasarch.akash.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.quasarch.akash.Akash;
import org.quasarch.akash.model.AkashPage;
import org.quasarch.akash.model.Bid;
import org.quasarch.akash.model.Deployment;
import org.quasarch.akash.model.DeploymentLease;
import org.quasarch.akash.model.OperationFailure;
import org.quasarch.akash.uri.QueryParam;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quasarch.akash.uri.UriUtils.addQueryParameters;

/**
 * Implementation of {@link Akash}.
 */
public final class AkashClient implements Akash {
    private final String accountAddress;
    private final URI baseUri;
    private final Function<URI,HttpRequest.Builder> httpRequestBuilderSupplier;

    public AkashClient(String accountAddress, URI baseUri, Function<URI,HttpRequest.Builder> httpRequestBuilderSupplier) {
        this.accountAddress = accountAddress;
        this.baseUri = baseUri;
        this.httpRequestBuilderSupplier = httpRequestBuilderSupplier;
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
    public Either<OperationFailure, AkashPage<Deployment>> listDeployments(Short page,
                                                                           Short resultPerPage,
                                                                           String deploymentSequence) {
        Objects.requireNonNull(deploymentSequence, "deploymentSequence cannot be empty on listDeployments");
        defaultsTo(page, () -> 0L);
        defaultsTo(resultPerPage, () -> 10L);


        return null;
    }

    @Override
    public Either<OperationFailure, Iterable<Deployment>> listDeployments(String deploymentSequence) {
        // TODO util to add query params

        var requestUri = addQueryParameters(
                baseUri,
                new QueryParam("filters.owner", accountAddress),
                new QueryParam("filters.dseq", deploymentSequence),
                // TODO what is state?
                new QueryParam("filters.state", null),
                new QueryParam("pagination.key", null),
                new QueryParam("pagination.offset", null),
                new QueryParam("pagination.limit", null),
                new QueryParam("pagination.count_total", null)
        );


        var request =
                httpRequestBuilderSupplier.apply(requestUri)
                .GET()
                .build();

        var bodyFuture = HttpClient
                .newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(AkashClient::fromBody);

        return getEither(bodyFuture);


    }

    @Override
    public Either<OperationFailure, Deployment> getDeployment(String owner, String dSeq) {
        return null;
    }

    @Override
    public Either<OperationFailure, AkashPage<Bid>> listBids(String deploymentSequence, String groupSequence, String oSeq, String providerId, String state, short page, short resultsPerPage) {
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

    private static Either<OperationFailure, Iterable<Deployment>> fromBody(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var deployments = List.of(objectMapper.readValue(body, Deployment[].class));
            return Either.right(deployments);
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
}
