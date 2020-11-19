package c0.util;

import c0.lexer.Token;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class RichIterator<T> implements Iterator<T> {
    protected Optional<T> bf;
    protected T stored;
    protected boolean isUnread = false;

    protected abstract Optional<T> getNext();

    @Override
    public boolean hasNext() {
        if (bf == null) {
            bf = getNext();
        }
        if (isUnread) {
            return true;
        }
        return bf.isPresent();
    }

    @Override
    public T next() {
        if (bf == null) {
            bf = getNext();
        }
        if (isUnread) {
            isUnread = false;
            return stored;
        }
        stored = bf.orElseThrow(NoSuchElementException::new);
        bf = getNext();
        if (stored instanceof Token) System.out.println(stored);
        return stored;
    }

    public T peek() {
        if (bf == null) {
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
