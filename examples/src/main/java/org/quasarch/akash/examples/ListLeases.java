package org.quasarch.akash.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListLeases {

    private static final Logger log = LoggerFactory.getLogger(ListLeases.class);

    public static void main(String[] args) {
        var akash = ExampleHelper.getClient();
        var result = akash.listLeases("akash1qpqpgv9zgesuxfx7sak32yuf0lmsytp9frysy9",
                null, null, null, null, null);
        if (result.isLeft()) {
            log.info("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        log.info("Printing/fetching leases ");
        result.get().forEach(x -> log.info("{}", x));

    }

}
