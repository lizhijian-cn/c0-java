package c0.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {
    TokenType tokenType;
    String value;

    public Token(TokenType tokenType) {
        this(tokenType, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TokenType tokenType) return this.tokenType == tokenType;
        if (!(o instanceof Token token)) return false;

        return tokenType == token.tokenType;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", tokenType, value == null ? "" : value);
    }
}
