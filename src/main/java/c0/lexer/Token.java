package c0.lexer;

import java.util.Optional;

public class Token {
    TokenType tokenType;
    Object value;

    public Token(TokenType tokenType) {
        this.tokenType = tokenType;
        this.value = null;
    }

    public Token(TokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public boolean equals(TokenType e) {
        return tokenType == e;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getString() {
        if (value instanceof String string) {
            return string;
        } else {
            throw new RuntimeException("token type is not STRING");
        }
    }

    public char getChar() {
        if (value instanceof Character ch) {
            return ch;
        } else {
            throw new RuntimeException("token type is not CHAR");
        }
    }

    public int getUInt() {
        if (value instanceof Integer i) {
            return i;
        } else {
            throw new RuntimeException("token type is not UINT");
        }
    }

    public double getDouble() {
        if (value instanceof Double d) {
            return d;
        } else {
            throw new RuntimeException("token type is not UINT");
        }
    }
}
