package org.quasarch.akash.examples;

import org.quasarch.akash.Akash;
import org.quasarch.akash.impl.AkashClient;

import java.net.URI;
import java.net.http.HttpClient;

public class ListDeployments {

    public static void main(String[] args) {
        Akash akash = new AkashClient("dummy-address",
                URI.create("https://akash.c29r3.xyz"),
                HttpClient::newHttpClient);
        var result = akash.listDeployments("akash1qqzwc5d7hynl67nsmn9jukvwqp3vzdl6j2t7lk", null, null);
        if (result.isLeft()) {
            System.out.println("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        System.out.println("Printing/fetching deployments " );
        result.get().forEach(System.out::println);


    }
}
