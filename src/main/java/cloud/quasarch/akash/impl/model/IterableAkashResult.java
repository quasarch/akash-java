package cloud.quasarch.akash.impl.model;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Represents an akash response which offers pagination based on
 * next_key.
 * For improved usability {@link Iterable} is implemented
 *
 * @param <T> the type of iterable
 */
public class IterableAkashResult<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() {
        return null;
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
