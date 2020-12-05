package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.ast.expr.ExprNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WhileNode extends StmtNode {
    ExprNode cond;
    BlockNode body;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
