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

//    public char getChar() {
//        if (value instanceof Character ch) {
//            return ch;
//        } else {
//            throw new RuntimeException("token type is not CHAR");
//        }
//    }
//
//    public int getUInt() {
//        if (value instanceof Integer i) {
//            return i;
//        } else {
//            throw new RuntimeException("token type is not UINT");
//        }
//    }
//
//    public double getDouble() {
//        if (value instanceof Double d) {
//            return d;
//        } else {
//            throw new RuntimeException("token type is not UINT");
//        }
//    }

    @Override
    public String toString() {
        return String.format("%s(%s)", tokenType, value == null ? "" : value);
    }
}
