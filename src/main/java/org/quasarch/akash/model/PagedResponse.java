package org.quasarch.akash.model;

import org.quasarch.akash.model.remote.PageInfo;

public interface PagedResponse<T> {
    PageInfo paginationInfo();

    Iterable<T> results();
}
