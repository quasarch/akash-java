package cloud.quasarch.akash.impl.model;

import cloud.quasarch.akash.impl.model.remote.PageInfo;

/**
 * Response which contains paged results in the form of an {@link Iterable}
 *
 * @param results        the iterable
 * @param paginationInfo information to be able to handle pages
 * @param <T>            type of data which was retrieved
 */
public record AkashPagedResponse<T>(Iterable<T> results,
                                    PageInfo paginationInfo) implements PagedResponse<T> {


}
