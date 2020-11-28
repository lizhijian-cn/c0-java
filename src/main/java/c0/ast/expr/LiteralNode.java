package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.error.UnreachableException;
import c0.lexer.Token;
import c0.type.Type;
import c0.type.TypeVal;
import lombok.Getter;

@Getter
public class LiteralNode extends ExprNode {
    Object value;

    public LiteralNode(String value) {
        this.type = new Type(TypeVal.STRING);
        this.value = value;
    }

    public LiteralNode(long value) {
        this.type = new Type(TypeVal.UINT);
        this.value = value;
    }

    public LiteralNode(double value) {
        this.type = new Type(TypeVal.DOUBLE);
        this.value = value;
    }

    public static LiteralNode FactoryConstructor(Token token) {
        return switch (token.getTokenType()) {
            case CHAR_LITERAL -> new LiteralNode(Character.getNumericValue(token.getChar()));
            case UINT_LITERAL -> new LiteralNode(token.getUInt());
            case STRING_LITERAL -> new LiteralNode(token.getString());
            case DOUBLE_LITERAL -> new LiteralNode(token.getDouble());
            default -> throw new UnreachableException();
        };
    }

    public static LiteralNode defaultValue(Type type) {
        return switch (type.getType()) {
            case UINT -> new LiteralNode(0);
            case DOUBLE -> new LiteralNode(0.0);
            default -> throw new RuntimeException("unsupported default value type");
        };
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
