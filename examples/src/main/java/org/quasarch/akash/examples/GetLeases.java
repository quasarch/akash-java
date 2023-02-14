package org.quasarch.akash.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetLeases {

    // must be an existing owner
    private static final String owner = "akash1qpqpgv9zgesuxfx7sak32yuf0lmsytp9frysy9";
    private static final String deploymentSequence = "9587519";
    private static final String providerId = "akash1cx9cc98ttn92wlj8zs5vyn2u7a5t8ray25cdpc";

    private static final Logger log = LoggerFactory.getLogger(GetLeases.class);

    public static void main(String[] args) {

        var akash = ExampleHelper.getClient();
        var result = akash.getLease(owner, deploymentSequence, null, null, providerId);
        if (result.isLeft()) {
            log.info("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        log.info("fetched lease: {}", result.get());
    }
}
