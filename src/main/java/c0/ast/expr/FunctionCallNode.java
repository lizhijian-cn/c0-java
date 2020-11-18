package c0.ast.expr;

import java.util.List;

public class FunctionCallNode extends ExprNode {
    String name;
    List<ExprNode> args;
    public FunctionCallNode(String name, List<ExprNode> args) {
        this.name = name;
        this.args = args;
    }
}
