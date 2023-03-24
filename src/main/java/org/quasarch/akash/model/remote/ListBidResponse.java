package org.quasarch.akash.model.remote;

/**
 * Response of a Bid request
 *
 * @param bids       {@link Bid} iterable
 * @param pagination {@link PageInfo}
 */
public record ListBidResponse(
        Iterable<Bid> bids,
        PageInfo pagination
) {

}
