package cloud.quasarch.akash.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.matchers.Times;
import cloud.quasarch.akash.impl.model.remote.AkashErrorType;

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
public class AkashClientTest {


    private MockServerClient client;


    @BeforeEach
    public void beforeEachLifecyleMethod(MockServerClient client) {
        this.client = client;
    }


    @Test
    public void createDeployment() {


    }

    @Test
    public void createDeploymentShouldThrowOnEmptySdlFile() {

        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), () -> HttpClient.newHttpClient());
        Assertions.assertThrows(NullPointerException.class, () -> instance.createDeployment(null));
    }

    @Test
    public void closedDeployment() {
    }


    /**
     * Happy path test
     */
    @Test
    public void listDeployments() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
       /*
        var instance = new AkashClient("some-address",
        URI.create("https://akash.c29r3.xyz"), HttpClient::newHttpClient);
        */
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
    public void listDeploymentsShouldMapHttpErrorsToFailure() throws IOException {
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
    public void getDeployment() throws IOException {
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
    public void getDeploymentShouldFailOnMissingArgument(String owner, String deploymentSequence) throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        assertThrows(NullPointerException.class, () -> instance.getDeployment(owner, deploymentSequence));

    }

    @Test
    public void itShouldFollowPaginationOnListBids() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);

        var response = Files.readString(Path.of("src/test/resources/responses/listBids-ok.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(2)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(response)
        );

        var result = instance.listBids("akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q", null,
                null, null, null, null);
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        // happy path
        assertFalse(result.isLeft());
        // json parsing
        var countBids = StreamSupport
                .stream(result.get().spliterator(), false)
                .count();
        assertEquals(8, countBids);
    }

    @Test
    public void itShouldListBids() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        /*var instance = new AkashClient("some-address",
        URI.create("https://akash.c29r3.xyz"), HttpClient::newHttpClient);*/

        var response = Files.readString(Path.of("src/test/resources/responses/listBids-ok.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(response)
        );

        var result = instance.listBids("akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q", null,
                null, null, null, null);
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        // happy path
        assertFalse(result.isLeft());
        // json parsing
        var firstBid = StreamSupport
                .stream(result.get().spliterator(), false)
                .findFirst();

        if (firstBid.isEmpty()) {
            fail("could not find a single bid result");
            return;
        }
        assertNotNull(firstBid.get().bid().bidId());
        assertNotNull(firstBid.get().escrowAccount().id());
    }

    /**
     * GET BID - Happy path
     */
    @Test
    public void itShouldGetBid() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        var response = Files.readString(Path.of("src/test/resources/responses/get-bid-ok.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(response)
        );
        var result = instance.getBid("akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q", "asd",
                null, null,
                "asdasd");
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        // happy path
        assertFalse(result.isLeft());
        // json parsing
        var bid = result.get();
        assertNotNull(bid.bid().bidId());
        assertNotNull(bid.escrowAccount().id());
    }


    @Test
    public void itShouldGetLease() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        var response = Files.readString(Path.of("src/test/resources/responses/get-lease-ok.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(response)
        );
        var result = instance.getLease("akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q", "asd",
                null, null,
                "asdasd");
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        // happy path
        assertFalse(result.isLeft());
        // json parsing
        var bid = result.get();
        assertNotNull(bid.leaseInfo().leaseId());
        assertNotNull(bid.escrowPayment().paymentId());
    }

    @Test
    public void itShouldListLeases() throws IOException {
        var instance = new AkashClient("some-address", URI.create("http://localhost:" + client.getPort()), HttpClient::newHttpClient);
        var response = Files.readString(Path.of("src/test/resources/responses/listLeases-ok.json"));
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(""), Times.exactly(1)
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(response)
        );

        var result = instance.listLeases("akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q", null,
                null, null, null, null);
        if (result.isLeft()) {
            fail("error was " + result.getLeft());
            return;
        }
        // happy path
        assertFalse(result.isLeft());
        // json parsing
        var firstLease = StreamSupport
                .stream(result.get().spliterator(), false)
                .findFirst();

        if (firstLease.isEmpty()) {
            fail("could not find a single lease result");
            return;
        }
        assertNotNull(firstLease.get().leaseInfo().leaseId().owner());
        assertNotNull(firstLease.get().escrowPayment().rate().amount());
    }


    public static Stream<Arguments> getDeploymentShouldFailOnMissingArgumentDataSupplier() {
        return Stream.of(
                Arguments.of("asd", null),
                Arguments.of(null, "seq"),
                Arguments.of(null, null)
        );
    }


}
