package cloud.quasarch.akash.impl.pagination;

import org.junit.jupiter.api.Test;
import cloud.quasarch.akash.impl.model.AkashPagedResponse;
import cloud.quasarch.akash.impl.model.remote.PageInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AkashPagedIterableTest {


    @Test
    public void itShouldCallNextOnBufferEnd() {
        var pageInfo = new PageInfo("token", 5);
        final var functionCallCount = new AtomicInteger();
        var instance = new AkashPagedIterable<>(
                (value) -> {
                    functionCallCount.incrementAndGet();
                    return new AkashPagedResponse<>(List.of("adeus"), pageInfo);
                },
                new AkashPagedResponse<>(List.of("ola"), pageInfo)
        );
        var resultList = StreamSupport
                .stream(instance.spliterator(), false)
                .toList();

        assertEquals(pageInfo.total(), resultList.size());
        assertEquals(4, functionCallCount.get());
    }

}
