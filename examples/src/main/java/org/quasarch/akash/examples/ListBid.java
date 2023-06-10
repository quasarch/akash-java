package org.quasarch.akash.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListBid {

    private static final Logger log = LoggerFactory.getLogger(ListBid.class);

    public static void main(String[] args) {
        var akash = ExampleHelper.getClient();
        var result = akash.listBids("akash1qqttharxgy9xjz9a28nlvs5rdhrcdchs2lfj5q",
                null, null, null, null, null);
        if (result.isLeft()) {
            log.info("Invocation failed, error is: " + result.getLeft());
            System.exit(0);
        }
        log.info("Printing/fetching bids ");
        result.get().forEach(x -> log.info("{}", x));

    }
}
