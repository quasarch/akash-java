package org.quasarch.akash.model;

/**
 * Represents a page request which is done based on a page number + results per page.
 * Prefer {@link PageByKey} when available
 *
 * @param currentPage
 * @param resultPerPage
 */
public record PageByOffset(short currentPage, short resultPerPage) {
}
