package org.quasarch.akash.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListDeployments {

    private static final Logger log = LoggerFactory.getLogger(ListDeployments.class);

    public static void main(String[] args) {
        var akash = ExampleHelper.getClient();
        var result = akash.listDeployments("akash1qqzwc5d7hynl67nsmn9jukvwqp3vzdl6j2t7lk", null, null);
        if (result.isLeft()) {
            log.info("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        log.info("Printing/fetching deployments ");
        result.get().forEach(x -> log.info("{}", x));
    }
}
