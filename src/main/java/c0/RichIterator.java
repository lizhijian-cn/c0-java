package c0;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public interface RichIterator<T> extends Iterator<T> {
    T peek();

    default T expect(T e) {
        if (test(e)) {
            return e;
        }
        throw new RuntimeException(
                String.format("expect %s, but get %s", e.toString(), hasNext() ? peek().toString() : "nothing"));
    }

    default boolean test(T e) {
        return test(x -> x.equals(e)).isPresent();
    }

    default Optional<T> test(Predicate<T> p) {
        if (!hasNext()) {
            return Optional.empty();
        }
        if (p.test(peek())) {
            return Optional.of(next());
        }
        return Optional.empty();
    }
}
