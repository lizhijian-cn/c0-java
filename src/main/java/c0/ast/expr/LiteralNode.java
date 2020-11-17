package c0.ast.expr;

import c0.type.Type;

public class LiteralNode extends ExprNode {
    public enum LiteralTypeVal {
        STRING, UINT, CHAR
    }

    LiteralTypeVal type;
    Object value;

    public static LiteralNode defaultValue(Type type) {
        return switch (type.getType()) {
            case INT -> new LiteralNode(0);
            default -> throw new RuntimeException("unsupported default value type");
        };
    }
    public LiteralNode(String value) {
        this.type = LiteralTypeVal.STRING;
        this.value = value;
    }

    public LiteralNode(char value) {
        this.type = LiteralTypeVal.CHAR;
        this.value = value;
    }

    public LiteralNode(long value) {
        this.type = LiteralTypeVal.UINT;
        this.value = value;
    }
}
