package org.quasarch.akash.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDeployment {

    // must be an existing owner
    private static final String owner = "akash1qqzwc5d7hynl67nsmn9jukvwqp3vzdl6j2t7lk";
    private static final String deploymentSequence = "1027706";

    private static final Logger log = LoggerFactory.getLogger(GetDeployment.class);

    public static void main(String[] args) {
        var akash = ExampleHelper.getClient();
        var result = akash.getDeployment(owner, deploymentSequence);
        log.info("fetched deployment: {}" , result.get());
    }
}
