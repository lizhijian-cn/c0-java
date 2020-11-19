package c0.ast.expr;

import c0.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FunctionCallNode extends ExprNode {
    String name;
    List<ExprNode> args;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
