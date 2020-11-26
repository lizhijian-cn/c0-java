package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.error.UnreachableException;
import c0.lexer.TokenType;
import lombok.Getter;

@Getter
public class BinaryOpNode extends ExprNode {
    ExprNode left;
    OpVal op;
    ExprNode right;

    public BinaryOpNode(ExprNode left, TokenType op, ExprNode right) {
        this.left = left;
        this.op = switch (op) {
            case LT -> OpVal.LT;
            case GT -> OpVal.GT;
            case LE -> OpVal.LE;
            case GE -> OpVal.GE;
            case EQ -> OpVal.EQ;
            case NEQ -> OpVal.NEQ;
            case PLUS -> OpVal.PLUS;
            case MINUS -> OpVal.MINUS;
            case MUL -> OpVal.MUL;
            case DIV -> OpVal.DIV;
            default -> throw new UnreachableException();
        };
        this.right = right;
    }

    @Override
    public <T, E> T accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
