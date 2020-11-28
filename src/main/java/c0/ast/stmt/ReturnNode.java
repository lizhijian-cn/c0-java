package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.ast.expr.ExprNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class ReturnNode extends StmtNode {
    Optional<ExprNode> returnValue;

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
