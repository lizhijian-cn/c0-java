package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.ast.expr.ExprNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExprStmtNode extends StmtNode {
    ExprNode expr;

    @Override
    public <T, E> E accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
