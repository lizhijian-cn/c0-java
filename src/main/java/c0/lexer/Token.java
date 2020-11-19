package c0.lexer;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TokenType tokenType) return this.tokenType == tokenType;
        if (!(o instanceof Token)) return false;

        Token token = (Token) o;

        return tokenType == token.tokenType;
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

    @Override
    public String toString() {
        return String.format("%s%s", tokenType, value == null ? "" : "(" + String.valueOf(value) + ")");
    }
}
