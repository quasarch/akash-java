package org.quasarch.akash.model.remote;

/**
 * @param denom  denomination aka currency
 * @param amount how much
 */
public record Price(
        String denom,
        String amount
) {

}
