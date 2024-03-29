package cloud.quasarch.akash.impl.parsing;

import java.util.Objects;
import java.util.function.Function;

/**
 * Builds instances of {@link ResponseParserWithTransformation}
 * Usage example (full, with intermediate operation)
 * <pre>
 * ResponseParserBuilder
 *                 {@code .<AkashPagedResponse<Deployment>, ListDeploymentsResponse>newBuilder()}
 *                 .withResultClass(ListDeploymentsResponse.class)
 *                 .withIntermediateOperation(
 *                         {@code intermediate -> new AkashPagedResponse<>(intermediate.deployments(),}
 *                                 intermediate.pagination())
 *                 .build()
 * </pre>
 * <p>
 * Usage example (simples, nointermediate operation)
 * <pre>
 * ResponseParserBuilder
 *                 {@code .<AkashPagedResponse<Deployment>, ListDeploymentsResponse>newBuilder() }
 *                 .withResultClass(ListDeploymentsResponse.class)
 *                 .build()
 * </pre>
 */
public class ResponseParserBuilder<R, I> {

    private Class<I> resultClass;
    private Function<I, R> transformation;

    private ResponseParserBuilder() {
    }

    /**
     * @param resultClass type of result class
     * @return ResponseParserBuilder
     */
    public ResponseParserBuilder<R, I> withResultClass(Class<I> resultClass) {
        this.resultClass = resultClass;
        return this;
    }

    /**
     * @param transformation Function which is an intermediaty operation
     * @return ResponseParserBuilder
     */
    public ResponseParserBuilder<R, I> withIntermediateOperation(Function<I, R> transformation) {
        this.transformation = transformation;
        return this;
    }

    /**
     * @return builds a {@link ResponseParser} instance
     */
    public ResponseParser<R> build() {
        if (resultClass == null) {
            throw new IllegalStateException("withResultClass must be called before build()");
        }
        return new ResponseParserWithTransformation<>(resultClass,
                Objects.requireNonNullElseGet(transformation, () -> inter -> (R) inter));

    }

    /**
     * @param <R> request class
     * @param <I> result class
     * @return instance of {@link ResponseParserBuilder}
     */
    public static <R, I> ResponseParserBuilder<R, I> newBuilder() {
        return new ResponseParserBuilder<>();
    }
}
