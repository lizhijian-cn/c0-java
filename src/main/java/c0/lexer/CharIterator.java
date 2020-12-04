package c0.lexer;

import c0.util.RichIterator;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Optional;


public class CharIterator extends RichIterator<Character> {
    BufferedReader br;

    public CharIterator(Reader reader) {
        this.br = new BufferedReader(reader);
    }

    @Override
    @SneakyThrows
    public Optional<Character> getNext() {
        var next = br.read();
        return next != -1 ? Optional.of((char) next) : Optional.empty();
    }
}
