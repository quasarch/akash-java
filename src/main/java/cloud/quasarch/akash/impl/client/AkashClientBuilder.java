package cloud.quasarch.akash.impl.client;

import cloud.quasarch.akash.AkashClient;
import shaded_package.org.apache.commons.lang3.StringUtils;

import java.net.URI;

public class AkashClientBuilder implements AkashClient.BuilderToApiBaseUrl, AkashClient.ApiBaseUrlToAccount, AkashClient.AccountToHttpClient {

    private String accountAddress;
    private String apiBaseUrl;

    private java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();

    public AkashClientBuilder withHttpClient(java.net.http.HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    @Override
    public AkashClientBuilder withApiBaseUrl(String apiBaseUrl) {
        if (StringUtils.isEmpty(apiBaseUrl)) {
            throw new IllegalArgumentException("apiBaseUrl should not be null or empty");
        }
        this.apiBaseUrl = apiBaseUrl;
        return this;
    }

    @Override
    public AkashClient.AccountToHttpClient forAccount(String accountAddress) {
        if (StringUtils.isEmpty(accountAddress)) {
            throw new IllegalArgumentException("accountAddress should not be null or empty");
        }
        this.accountAddress = accountAddress;
        return this;
    }

    public AkashClient build() {
        return new DefaultAkashClient(this.accountAddress, URI.create(this.apiBaseUrl), this.httpClient);
    }
}
