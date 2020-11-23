package c0.ast.expr;

import c0.entity.Function;
import c0.visitor.Visitor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class FunctionCallNode extends ExprNode {
    String name;
    @Setter
    Function function;
    List<ExprNode> args;

    public FunctionCallNode(String name, List<ExprNode> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
