package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.error.UnreachableException;
import c0.lexer.TokenType;
import lombok.Getter;

@Getter
public class UnaryOpNode extends ExprNode {
    OpVal op;
    ExprNode expr;

    public UnaryOpNode(TokenType op, ExprNode expr) {
        this.op = switch (op) {
            case MINUS -> OpVal.NEG;
            default -> throw new UnreachableException();
        };
        this.expr = expr;
    }

    @Override
    public <T, E> T accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
