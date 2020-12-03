package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.entity.StringVariable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class STLFunctionCallNode extends ExprNode {
    StringVariable function;
    List<ExprNode> args;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
