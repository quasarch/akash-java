package org.quasarch.akash.impl.pagination;

import org.jetbrains.annotations.NotNull;
import org.quasarch.akash.model.PagedResponse;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Iterable implementation take supports fetching more data upstream.
 * It abstracts the concept of page, using next key.
 *
 * @param <T>
 */
public class AkashPagedIterable<T> implements Iterable<T> {

    private final Function<String, PagedResponse<T>> dataFetcher;
    private final PagedResponse<T> firstPage;


    public AkashPagedIterable(Function<String, PagedResponse<T>> dataFetcher, PagedResponse<T> firstPage) {
        this.dataFetcher = dataFetcher;
        this.firstPage = firstPage;
    }


    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new AkashPagedIterator<>(dataFetcher, firstPage);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }


}


