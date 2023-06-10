package cloud.quasarch.akash.impl.client;

import cloud.quasarch.akash.AkashClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AkashClientBuilderTest {

    private static final String API_BASE_URL = "https://akash-api.polkachu.com";
    private static final String ACCOUNT_ADDRESS = "akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q";

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowNullPointerExceptionWhenProvidedApiBaseUrlIsNullOrEmpty(String apiBaseUrl) {
        assertThatThrownBy(
                () -> AkashClient.builder()
                        .withApiBaseUrl(apiBaseUrl)
                        .forAccount(null)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("apiBaseUrl should not be null or empty");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowNullPointerExceptionWhenProvidedAccountAddressIsNullOrEmpty(String accountAddress) {
        assertThatThrownBy(
                () -> AkashClient.builder()
                        .withApiBaseUrl(API_BASE_URL)
                        .forAccount(accountAddress)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("accountAddress should not be null or empty");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenProvidedHttpClientSupplierIsNull() {
        assertThatThrownBy(
                () -> AkashClient.builder()
                        .withApiBaseUrl(API_BASE_URL)
                        .forAccount(ACCOUNT_ADDRESS)
                        .withHttpClient(null)
                        .build()
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBuildAkashClientInstanceWithCustomApiUrl() {
        final AkashClient akashClient = AkashClient.builder()
                .withApiBaseUrl(API_BASE_URL)
                .forAccount(ACCOUNT_ADDRESS)
                .build();

        assertThat(akashClient)
                .usingRecursiveComparison()
                .comparingOnlyFields("accountAddress", "apiBaseUri")
                .isEqualTo(
                        new DefaultAkashClient(
                                ACCOUNT_ADDRESS,
                                URI.create(API_BASE_URL)
                        )
                );
    }
}