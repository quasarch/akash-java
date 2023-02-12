package org.quasarch.akash.model.remote;

public record ListBidResponse(
        Iterable<Bid> bids,
        PageInfo pagination
) {

}
