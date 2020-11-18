package c0.ast.stmt;

import c0.ast.expr.ExprNode;

public class ReturnNode extends StmtNode {
    ExprNode returnValue;

    public ReturnNode(ExprNode returnValue) {
        this.returnValue = returnValue;
    }
}
