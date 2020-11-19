package c0;

import c0.lexer.CharIterator;
import org.junit.Test;
import org.junit.Assert;
import java.io.StringReader;

public class CharIteratorTest {
    @Test
    public void testRichIterator() {
        var reader = new StringReader("""
                a+c;
                """);
        var charIter = new CharIterator(reader);
        Assert.assertEquals(Character.valueOf('a'), charIter.peek());
        Assert.assertEquals(Character.valueOf('a'), charIter.next());
        Assert.assertEquals(Character.valueOf('+'), charIter.next());
        charIter.unread();
        Assert.assertEquals(Character.valueOf('+'), charIter.peek());
        Assert.assertEquals(Character.valueOf('+'), charIter.next());
        Assert.assertEquals(Character.valueOf('c'), charIter.next());
    }
}
