package c0;


import c0.lexer.CharIterator;
import org.junit.Test;

import java.io.StringReader;

public class LexerTest {
    @Test
    public void testRichIterator() {
        var reader = new StringReader("""
                int i = 1;
                fn f(a: int) -> int {
                    return -a+a*a;
                }
                fn func(a: int, b: int) -> void {
                    i = f(a) == f(b);
                    return;
                }
                """);
        var charIter = new CharIterator(reader);
    }
}
