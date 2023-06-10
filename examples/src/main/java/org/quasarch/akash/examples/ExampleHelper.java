package org.quasarch.akash.examples;

import cloud.quasarch.akash.impl.Akash;
import cloud.quasarch.akash.impl.AkashClient;

import java.net.URI;
import java.net.http.HttpClient;

public class ExampleHelper {

    public static Akash getClient() {

        return new AkashClient("dummy-address",
                URI.create("https://akash.c29r3.xyz"),
                HttpClient::newHttpClient);

    }
}
