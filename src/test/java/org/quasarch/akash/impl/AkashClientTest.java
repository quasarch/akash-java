package org.quasarch.akash.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.matchers.Times;
import org.quasarch.akash.model.remote.AkashErrorType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * test for {@link AkashClient} implementation
 */

@ExtendWith({MockitoExtension.class, MockServerExtension.class})
class AkashClientTest {


    private MockServerClient client;


    @BeforeEach
    public void beforeEachLifecyleMethod(MockServerClient client) {
        this.client = client;
    }


    @Test
    void createDeployment() {


    }

    @Test
    void createDeploymentShouldThrowOnEmptySdlFile() {

        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), () -> HttpClient.newHttpClient());
        Assertions.assertThrows(NullPointerException.class, () -> instance.createDeployment(null));
    }

    @Test
    void closedDeployment() {
    }


    /**
     * Happy path test
     */
    @Test
    void listDeployments() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
       /* var instance = new AkashClient("some-address",
                URI.create("https://akash.c29r3.xyz"), HttpClient::newHttpClient);*/
        var response = Files.readString(Path.of("src/test/resources/responses/listDeployments-6-returns.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(response)
        );

        var result = instance.listDeployments("akash1qqzwc5d7hynl67nsmn9jukvwqp3vzdl6j2t7lk", null, null);
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        final AtomicInteger count = new AtomicInteger();
        result.get().forEach(deployment -> {
            count.incrementAndGet();
        });


        // happy path
        assertFalse(result.isLeft());

        // json parsing


    }

    /**
     * code 3 should be mapped to BadRequest
     *
     * @throws IOException
     */
    @Test
    void listDeploymentsShouldMapHttpErrorsToFailure() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        var response = Files.readString(Path.of("src/test/resources/responses/listDeployments-error.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(400)
                        .withBody(response)
        );
        var result = instance.listDeployments("", null, null);

        assertEquals(AkashErrorType.BadRequest, result.getLeft().errorType());


    }

    /**
     * Happy path
     */
    @Test
    void getDeployment() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);

        var responseBody = Files.readString(Path.of("src/test/resources/responses/info-deployment-ok-return.json"));
        client.when(
                request()

                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(responseBody)
        );

        var result = instance.getDeployment("akash1qqzwc5d7hynl67nsmn9jukvwqp3vzdl6j2t7lk", "dseq");
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        // happy path
        assertFalse(result.isLeft());
        // json parsing sampling
        var response = result.get();
        assertEquals("akash1qqzwc5d7hynl67nsmn9jukvwqp3vzdl6j2t7lk", response.deployment().deploymentId().owner());
        assertEquals("1027706", response.deployment().deploymentId().deploymentSequence());
        assertEquals("closed", response.escrowAccount().state());

    }

    @ParameterizedTest
    @MethodSource("getDeploymentShouldFailOnMissingArgumentDataSupplier")
    void getDeploymentShouldFailOnMissingArgument(String owner, String deploymentSequence) throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        assertThrows(NullPointerException.class, () -> instance.getDeployment(owner, deploymentSequence));

    }

    @Test
    void listBids() {
    }

    @Test
    void testListBids() {
    }

    @Test
    void getLease() {
    }


    public static Stream<Arguments> getDeploymentShouldFailOnMissingArgumentDataSupplier() {
        return Stream.of(
                Arguments.of("asd", null),
                Arguments.of(null, "seq"),
                Arguments.of(null, null)
        );
    }

}
