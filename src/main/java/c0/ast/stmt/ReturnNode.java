package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.ast.expr.ExprNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReturnNode extends StmtNode {
    ExprNode returnValue;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
