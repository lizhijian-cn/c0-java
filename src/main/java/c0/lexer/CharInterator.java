package c0.lexer;

import c0.RichIterator;


public class CharInterator implements RichIterator<Character> {
    char[] cs;
    int i;
    @Override
    public Character next() {
        return cs[i++];
    }

    @Override
    public boolean hasNext() {
        return i < cs.length;
    }

    @Override
    public Character peek() {
        return cs[i];
    }
}
