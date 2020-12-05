package c0;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class RichIterator<T> implements Iterator<T> {
    private Optional<T> bf;
    private T stored;
    private boolean isUnread = false;
    private boolean init = false;

    protected abstract Optional<T> getNext();

    @Override
    public boolean hasNext() {
        if (!init) {
            init = true;
            bf = getNext();
        }
        if (isUnread) {
            return true;
        }
        return bf.isPresent();
    }

    @Override
    public T next() {
        if (!init) {
            init = true;
            bf = getNext();
        }
        if (isUnread) {
            isUnread = false;
            return stored;
        }
        stored = bf.orElseThrow(NoSuchElementException::new);
        bf = getNext();
        return stored;
    }

    public T peek() {
        if (!init) {
            init = true;
            bf = getNext();
        }
        if (isUnread) {
            return stored;
        }
        return bf.orElseThrow(NoSuchElementException::new);
    }

    public void unread() {
        if (isUnread) {
            throw new RuntimeException("can not unread twice");
        }
        isUnread = true;
    }

    public <E> T expect(E e) {
        if (check(e)) {
            return next();
        }
        throw new RuntimeException(
                String.format("expect %s, but get %s", e.toString(), hasNext() ? peek().toString() : "nothing"));
    }

    public <E> boolean check(E e) {
        return check(x -> x.equals(e));
    }

    public boolean check(Predicate<T> p) {
        if (!hasNext()) {
            return false;
        }
        return p.test(peek());
    }

    public <E> boolean test(E e) {
        boolean res = check(e);
        if (res) {
            next();
        }
        return res;
    }
}
