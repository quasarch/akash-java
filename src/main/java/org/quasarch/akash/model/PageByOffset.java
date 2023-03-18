package org.quasarch.akash.model;

/**
 * Represents a page request which is done based on a page number + results per page.
 * Prefer {@link PageByKey} when available
 *
 * @param currentPage the page we ask
 * @param resultPerPage how many results per page
 */
public record PageByOffset(short currentPage, short resultPerPage) {
}
