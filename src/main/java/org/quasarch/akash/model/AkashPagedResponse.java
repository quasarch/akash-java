package org.quasarch.akash.model;

import org.quasarch.akash.model.remote.PageInfo;

public record AkashPagedResponse<T>(Iterable<T> results,
                                    PageInfo paginationInfo) implements PagedResponse<T> {


}
