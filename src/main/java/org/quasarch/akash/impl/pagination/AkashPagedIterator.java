package org.quasarch.akash.impl.pagination;

import org.quasarch.akash.model.PagedResponse;
import org.quasarch.akash.model.remote.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Iterator for {@link org.quasarch.akash.model.AkashPagedResponse}
 *
 * @param <T> the type of data
 */
public class AkashPagedIterator<T> implements Iterator<T> {
    private static final Logger log = LoggerFactory.getLogger(AkashPagedIterator.class);
    private final Function<String, PagedResponse<T>> dataFetcher;
    private PageInfo currentPage;
    private Iterator<T> buffer;
    private int globalOffset = 0;

    /**
     * constructor
     *
     * @param dataFetcher function that knows how to fetch more data
     * @param firstPage   the initial, already loaded, page
     */
    public AkashPagedIterator(Function<String, PagedResponse<T>> dataFetcher, PagedResponse<T> firstPage) {
        this.dataFetcher = dataFetcher;
        currentPage = firstPage.paginationInfo();
        buffer = firstPage.results().iterator();
    }


    @Override
    public boolean hasNext() {
        log.debug("hasNext called");
        log.trace("globalOffset is {}", globalOffset);
        log.trace("currentPage.total is {}", currentPage.total());
        return globalOffset != currentPage.total();
    }

    @Override
    public T next() {
        log.debug("next called");
        log.trace("globalOffset is {}", globalOffset);
        globalOffset++;
        // assuming next will not be called without previously calling hasNext()
        if (!buffer.hasNext()) {
            fetchNextPage();
        }
        return buffer.next();


    }

    private void fetchNextPage() {
        log.debug("fetchNextPage");
        final var result = dataFetcher.apply(currentPage.nextKey());
        currentPage = result.paginationInfo();
        buffer = result.results().iterator();
    }


}
