//package c0.util;
//
//import java.util.Iterator;
//import java.util.function.Predicate;
//
//
//@Deprecated
//public interface RichIteratorVersion1_0<T> extends Iterator<T> {
//    T peek();
//
//    default <E> T expect(E e) {
//        if (check(e)) {
//            return next();
//        }
//        throw new RuntimeException(
//                String.format("expect %s, but get %s", e.toString(), hasNext() ? peek().toString() : "nothing"));
//    }
//
//    default <E> boolean check(E e) {
//        return check(x -> x.equals(e));
//    }
//
//    default boolean check(Predicate<T> p) {
//        if (!hasNext()) {
//            return false;
//        }
//        return p.test(peek());
//    }
//
//    default <E> boolean test(E e) {
//        boolean res = check(e);
//        if (res) {
//            next();
//        }
//        return res;
//    }
//}
