package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.error.UnreachableException;
import c0.lexer.Token;
import c0.type.Type;
import c0.type.TypeVal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LiteralNode extends ExprNode {
    String value;

    public LiteralNode(Type type) {
        this(type, switch (type.getType()) {
            case UINT -> "0";
            case DOUBLE -> "0.0";
            default -> throw new UnreachableException();
        });
    }

    public LiteralNode(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public static LiteralNode FactoryConstructor(Token token) {
        return switch (token.getTokenType()) {
            case CHAR_LITERAL, UINT_LITERAL -> new LiteralNode(new Type(TypeVal.UINT), token.getValue());
            case DOUBLE_LITERAL -> new LiteralNode(new Type(TypeVal.DOUBLE), token.getValue());
            default -> throw new UnreachableException();
        };
    }
//    Object value;
//
//    public LiteralNode(String value) {
//        this.type = new Type(TypeVal.STRING);
//        this.value = value;
//    }
//
//    public LiteralNode(long value) {
//        this.type = new Type(TypeVal.UINT);
//        this.value = value;
//    }
//
//    public LiteralNode(double value) {
//        this.type = new Type(TypeVal.DOUBLE);
//        this.value = value;
//    }
//

//
//    public static LiteralNode defaultValue(Type type) {
//        return switch (type.getType()) {
//            case UINT -> new LiteralNode(0);
//            case DOUBLE -> new LiteralNode(0.0);
//            default -> throw new RuntimeException("unsupported default value type");
//        };
//    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
