package org.quasarch.akash.model;

import org.quasarch.akash.model.remote.PageInfo;

/**
 * A wrapper for paged results
 *
 * @param <T> the data of the type which will be iterated
 */
public interface PagedResponse<T> {
    /**
     * getter
     *
     * @return Useful information about pagination settings/options
     */
    PageInfo paginationInfo();

    /**
     * getter
     *
     * @return the actual results. Implementation will be responsible for consuming the full result dataset
     */
    Iterable<T> results();
}
