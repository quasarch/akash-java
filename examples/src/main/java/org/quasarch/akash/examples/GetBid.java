package org.quasarch.akash.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBid {

    // must be an existing owner
    private static final String owner = "akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q";
    private static final String deploymentSequence = "4738338";
    private static final String providerId = "akash1qhjtxmacslmefm3v4sn5ggq6ed9jn83cy2rjd0";

    private static final Logger log = LoggerFactory.getLogger(GetBid.class);

    public static void main(String[] args) {

        var akash = ExampleHelper.getClient();
        var result = akash.getBid(owner, deploymentSequence, null, null, providerId);
        if (result.isLeft()) {
            log.info("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        log.info("fetched bid: {}", result.get());
    }
}
