package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.ast.expr.ExprNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class IfNode extends StmtNode {
    ExprNode cond;
    BlockNode thenBody;
    Optional<BlockNode> elseBody;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
