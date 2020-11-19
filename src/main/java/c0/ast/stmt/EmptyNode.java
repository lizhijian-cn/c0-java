package c0.ast.stmt;

import c0.visitor.Visitor;

public class EmptyNode extends StmtNode {

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
